package net.local.poc.sse.application.usecase;

import java.util.stream.Stream;

import net.local.poc.sse.application.dto.CustomerResponse;
import net.local.poc.sse.domain.ports.LoadCustomerPort;

public class ListCustomerUC {

    private final LoadCustomerPort loadCustomerPort;

    public ListCustomerUC(LoadCustomerPort loadCustomerPort) {
        this.loadCustomerPort = loadCustomerPort;
    }

    public Stream<CustomerResponse> handle() {
        return loadCustomerPort.load()
                               .map(obj -> new CustomerResponse(obj.getCustomerId(), obj.getName(), obj.getEmail()));
    }
}
