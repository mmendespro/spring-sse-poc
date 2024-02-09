package net.local.poc.sse.application.usecase;

import java.util.UUID;

import net.local.poc.sse.application.dto.CustomerResponse;
import net.local.poc.sse.domain.ports.LoadCustomerPort;

public class DetailCustomerUC {
    
    private final LoadCustomerPort loadCustomerPort;

    public DetailCustomerUC(LoadCustomerPort loadCustomerPort) {
        this.loadCustomerPort = loadCustomerPort;
    }

    public CustomerResponse handle(UUID customerId) {
        var customer = loadCustomerPort.load(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        return new CustomerResponse(customer.getCustomerId(), customer.getName(), customer.getEmail());
    }
}
