package ru.practicum.telemetry.collector.service.sensor;

import ru.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEventType;

public interface SensorEventHandler {
    SensorEventType getMessageType();
    void handle(SensorEvent event);
}
