package ru.practicum.telemetry.collector.service.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.dto.sensor.ClimateSensorEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEventType;
import ru.practicum.telemetry.collector.kafka.KafkaClientProducer;
import ru.practicum.telemetry.collector.service.sensor.BaseSensorEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {
    public ClimateSensorEventHandler(KafkaClientProducer producer) {
        super(producer);
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEvent event) {
        ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) event;
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(climateSensorEvent.getCo2Level())
                .setHumidity(climateSensorEvent.getHumidity())
                .setTemperatureC(climateSensorEvent.getTemperatureC())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
