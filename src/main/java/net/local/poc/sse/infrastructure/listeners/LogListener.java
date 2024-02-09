package net.local.poc.sse.infrastructure.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.local.poc.sse.domain.events.InternalEvent;

@Slf4j
@Component
public class LogListener {
    
    @Async
    @EventListener
    void onEvent(InternalEvent event) {
        if (event.isSuccess()) {
            log.info("Event success {}", event.toJson());
        } else {
            log.error("Event error {}", event.getException());
        }
    }
}
