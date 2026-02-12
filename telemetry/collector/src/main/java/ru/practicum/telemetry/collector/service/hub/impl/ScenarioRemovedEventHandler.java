package ru.practicum.telemetry.collector.service.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.dto.hub.HubEventType;
import ru.practicum.telemetry.collector.dto.hub.scenario.ScenarioRemovedEvent;
import ru.practicum.telemetry.collector.kafka.KafkaClientProducer;
import ru.practicum.telemetry.collector.service.hub.BaseHubEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {
    public ScenarioRemovedEventHandler(KafkaClientProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
        ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
