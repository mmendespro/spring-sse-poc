package net.local.poc.sse.infrastructure.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.local.poc.sse.domain.events.EventPublisher;
import net.local.poc.sse.domain.events.InternalEvent;

@Slf4j
@Component
public class AppEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public AppEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishEvent(InternalEvent event) {
        log.info("Publish event {}", event.toJson());
        applicationEventPublisher.publishEvent(event);
    }
    
}
