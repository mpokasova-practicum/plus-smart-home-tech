package ru.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.dto.hub.HubEventType;
import ru.practicum.telemetry.collector.dto.sensor.SensorEvent;
import ru.practicum.telemetry.collector.dto.sensor.SensorEventType;
import ru.practicum.telemetry.collector.service.hub.HubEventHandler;
import ru.practicum.telemetry.collector.service.sensor.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class CollectorController {
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlerMap;
    private final Map<HubEventType, HubEventHandler> hubEventHandlerMap;

    public CollectorController(List<SensorEventHandler> sensorEventHandlerList, List<HubEventHandler> hubEventHandlerList) {
        this.sensorEventHandlerMap = sensorEventHandlerList.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlerMap = hubEventHandlerList.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }


    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        if (sensorEventHandlerMap.containsKey(event.getType())) {
            sensorEventHandlerMap.get(event.getType()).handle(event);
        } else {
            throw new IllegalArgumentException("Не найден обработчик для события " + event.getType());
        }
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        if (hubEventHandlerMap.containsKey(event.getType())) {
            hubEventHandlerMap.get(event.getType()).handle(event);
        } else {
            throw new IllegalArgumentException("Не найден обработчик для события " + event.getType());
        }
    }
}
