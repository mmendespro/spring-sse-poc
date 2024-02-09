package net.local.poc.sse.application.usecase;

import java.util.UUID;

import net.local.poc.sse.application.dto.CustomerRequest;
import net.local.poc.sse.application.dto.CustomerResponse;
import net.local.poc.sse.domain.entities.Customer;
import net.local.poc.sse.domain.events.CustomerEvent;
import net.local.poc.sse.domain.events.EventPublisher;
import net.local.poc.sse.domain.ports.LoadCustomerPort;
import net.local.poc.sse.domain.ports.SaveCustomerPort;

public class CreateCustomerUC {
    
    private final EventPublisher publisher;
    private final LoadCustomerPort loadCustomerPort;
    private final SaveCustomerPort saveCustomerPort;

    public CreateCustomerUC(EventPublisher publisher, LoadCustomerPort loadCustomerPort, SaveCustomerPort saveCustomerPort) {
        this.publisher = publisher;
        this.loadCustomerPort = loadCustomerPort;
        this.saveCustomerPort = saveCustomerPort;
    }

    public CustomerResponse handle(CustomerRequest request) {
        loadCustomerPort.load(request.name()).ifPresent(cust -> {
            throw new RuntimeException(String.format("Customer % already exist", cust.getName()));
        });

        var customer = new Customer(UUID.randomUUID(), request.name(), request.email());

        saveCustomerPort.save(customer);

        publisher.publishEvent(new CustomerEvent(customer));

        return new CustomerResponse(customer.getCustomerId(), customer.getName(), customer.getEmail());
    }
}
