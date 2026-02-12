package ru.practicum.telemetry.collector.service.hub;

import ru.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.dto.hub.HubEventType;

public interface HubEventHandler {
    HubEventType getMessageType();
    void handle(HubEvent event);
}
