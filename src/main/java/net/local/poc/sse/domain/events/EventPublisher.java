package net.local.poc.sse.domain.events;

public interface EventPublisher {
    
    public void publishEvent(InternalEvent event);
}
