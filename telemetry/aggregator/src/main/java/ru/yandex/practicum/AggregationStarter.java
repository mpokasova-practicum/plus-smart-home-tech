package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final SnapshotStorage snapshotStorage;

    @Value("${kafka.input-topic}")
    private String inputTopic;
    @Value("${kafka.output-topic}")
    private String outputTopic;

    public void start() {
        try {
            consumer.subscribe(List.of(inputTopic));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, SensorEventAvro> consumerRecord : consumerRecords) {
                    try {
                        Optional<SensorsSnapshotAvro> snapshotOpt = snapshotStorage.updateState(consumerRecord.value());

                        if (snapshotOpt.isPresent()) {
                            SensorsSnapshotAvro snapshot = snapshotOpt.get();
                            ProducerRecord<String, SensorsSnapshotAvro> producerRecord = new ProducerRecord<>(
                                    outputTopic, snapshot.getHubId(), snapshot);

                            producer.send(producerRecord);
                        }
                    } catch (Exception e) {
                        log.error("Ошибка при обработке записи: ключ={}, значение={}", consumerRecord.key(), consumerRecord.value(), e);
                    }
                }
                consumer.commitSync();
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
                log.info("Перед закрытием producer'а все данные отправлены в kafka");
                consumer.commitSync();
                log.info("Перед закрытие consumer'а все смещения зафиксированы");
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }
}
