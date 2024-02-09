package net.local.poc.sse.domain.ports;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import net.local.poc.sse.domain.entities.Customer;

public interface LoadCustomerPort {
    Stream<Customer> load();
    Optional<Customer> load(UUID customerId);
    Optional<Customer> load(String customerName);
}
