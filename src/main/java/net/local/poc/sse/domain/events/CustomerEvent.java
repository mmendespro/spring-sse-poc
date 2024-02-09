package net.local.poc.sse.domain.events;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.local.poc.sse.domain.entities.Customer;

public class CustomerEvent extends InternalEvent {

    private final Customer customer;

    public CustomerEvent(Customer customer) {
        startTimer();
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }    

    @Override
    public Object getSource() {
        return getCustomer();
    }

    @Override
    public String toJson() {
        try {
            var mapper = new ObjectMapper();
            Map<String, Object> message = new HashMap<>(Map.of("event", getOrigin()));
            message.put("content", getCustomer());
            message.put("elapsedTimeInMilli", getElapsedTimeInMilli());
            if (hasError()) {
                message.put("message", getException().getMessage());
            }
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException jsonException) {
            return String.format("%s - %s", customer, jsonException);
        }
    }    
}
