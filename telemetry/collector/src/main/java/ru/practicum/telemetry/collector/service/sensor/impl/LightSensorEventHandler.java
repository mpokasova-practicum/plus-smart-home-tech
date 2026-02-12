package ru.practicum.telemetry.collector.service.sensor.impl;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.dto.sensor.LightSensorEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEventType;
import ru.practicum.telemetry.collector.kafka.KafkaClientProducer;
import ru.practicum.telemetry.collector.service.sensor.BaseSensorEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {
    public LightSensorEventHandler(KafkaClientProducer producer) {
        super(producer);
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEvent event) {
        LightSensorEvent lightSensorAvro = (LightSensorEvent) event;
        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensorAvro.getLinkQuality())
                .setLuminosity(lightSensorAvro.getLuminosity())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
