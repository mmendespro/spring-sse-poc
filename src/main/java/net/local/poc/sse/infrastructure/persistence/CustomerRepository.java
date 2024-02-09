package net.local.poc.sse.infrastructure.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import net.local.poc.sse.domain.entities.Customer;
import net.local.poc.sse.domain.ports.LoadCustomerPort;
import net.local.poc.sse.domain.ports.SaveCustomerPort;

@Repository
public class CustomerRepository implements LoadCustomerPort, SaveCustomerPort {

    private final Map<UUID, Customer> memDB = new HashMap<>();

    @Override
    public Stream<Customer> load() {
        return memDB.values().stream();
    }

    @Override
    public Optional<Customer> load(UUID customerId) {
        return memDB.values()
                    .stream()
                    .filter(cust -> cust.getCustomerId().equals(customerId))
                    .findFirst();
    }

    @Override
    public Optional<Customer> load(String customerName) {
        return memDB.values()
                    .stream()
                    .filter(cust -> cust.getName().equals(customerName))
                    .findFirst();
    }

    @Override
    public void save(Customer customer) {
        memDB.put(customer.getCustomerId(), customer);
    }    
}
