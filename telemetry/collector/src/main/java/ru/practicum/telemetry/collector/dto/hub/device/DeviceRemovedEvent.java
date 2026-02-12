package ru.practicum.telemetry.collector.dto.hub.device;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.dto.hub.HubEventType;

@Getter @Setter @ToString(callSuper = true)
public class DeviceRemovedEvent extends HubEvent {
    @NotNull
    private String id;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
