package ru.practicum.telemetry.collector.service.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEventType;
import ru.practicum.telemetry.collector.dto.sensor.TemperatureSensorEvent;
import ru.practicum.telemetry.collector.kafka.KafkaClientProducer;
import ru.practicum.telemetry.collector.service.sensor.BaseSensorEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {
    public TemperatureSensorEventHandler(KafkaClientProducer producer) {
        super(producer);
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent event) {
        TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) event;
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
