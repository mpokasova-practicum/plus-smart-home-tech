package ru.practicum.telemetry.collector.dto.hub.scenario;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioCondition {
    @NotBlank
    private String sensorId;
    private ConditionType type;
    private Operation operation;
    private int value;
}
