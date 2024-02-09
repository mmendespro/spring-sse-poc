package net.local.poc.sse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.local.poc.sse.application.usecase.CreateCustomerUC;
import net.local.poc.sse.application.usecase.DetailCustomerUC;
import net.local.poc.sse.application.usecase.ListCustomerUC;
import net.local.poc.sse.domain.events.EventPublisher;
import net.local.poc.sse.domain.ports.LoadCustomerPort;
import net.local.poc.sse.domain.ports.SaveCustomerPort;

@Configuration
public class ApplicationConfig {
    
    @Bean
    public ListCustomerUC listCustomerUC(LoadCustomerPort loadCustomerPort) {
        return new ListCustomerUC(loadCustomerPort);
    }

    @Bean
    public DetailCustomerUC detailCustomerUC(LoadCustomerPort loadCustomerPort) {
        return new DetailCustomerUC(loadCustomerPort);
    }

    @Bean
    public CreateCustomerUC createCustomerUC(EventPublisher publisher, LoadCustomerPort loadCustomerPort, SaveCustomerPort saveCustomerPort) {
        return new CreateCustomerUC(publisher, loadCustomerPort, saveCustomerPort);
    }
}
