package net.local.poc.sse.domain.ports;

import net.local.poc.sse.domain.entities.Customer;

public interface SaveCustomerPort {
    
    void save(Customer customer);
}
