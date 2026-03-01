package ru.yandex.practicum.handlers.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceAddedHandler implements HubEventHandler {
    private final SensorRepository repository;

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        log.info("Сохраняем новое устройство для хаба с id = {}", event.getHubId());
        DeviceAddedEventAvro addedEvent = (DeviceAddedEventAvro) event.getPayload();
        if (repository.existsByIdInAndHubId(List.of(addedEvent.getId()), event.getHubId())) {
            log.info("Устройство с id = {} уже существует для хаба {}, пропускаем", addedEvent.getId(), event.getHubId());
            return;
        }
        repository.save(mapToSensor(event));
    }

    @Override
    public String getPayloadType() {
        return DeviceAddedEventAvro.class.getSimpleName();
    }

    private Sensor mapToSensor(HubEventAvro event) {
        DeviceAddedEventAvro deviceAddedEvent = (DeviceAddedEventAvro) event.getPayload();

        return Sensor.builder()
                .id(deviceAddedEvent.getId())
                .hubId(event.getHubId())
                .build();
    }
}