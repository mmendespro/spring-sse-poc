package net.local.poc.sse.adapters.http.api;

import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.local.poc.sse.application.dto.CustomerRequest;
import net.local.poc.sse.application.dto.CustomerResponse;
import net.local.poc.sse.application.usecase.CreateCustomerUC;
import net.local.poc.sse.application.usecase.DetailCustomerUC;
import net.local.poc.sse.application.usecase.ListCustomerUC;


@Slf4j
@RestController
@RequestMapping("v1/customers")
public class CustomerController {
    
    private final ListCustomerUC listCustomerUC;
    private final CreateCustomerUC createCustomerUC;
    private final DetailCustomerUC detailCustomerUC;

    public CustomerController(ListCustomerUC listCustomerUC, CreateCustomerUC createCustomerUC,
            DetailCustomerUC detailCustomerUC) {
        this.listCustomerUC = listCustomerUC;
        this.createCustomerUC = createCustomerUC;
        this.detailCustomerUC = detailCustomerUC;
    }

    @GetMapping
    public ResponseEntity<Stream<CustomerResponse>> loadCustomers() {
        try {
            return new ResponseEntity<>(listCustomerUC.handle(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<CustomerResponse> loadCustomer( @PathVariable(name = "customerId") UUID customerId) {
        try {
            return new ResponseEntity<>(detailCustomerUC.handle(customerId), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest request) {
        try {
            return new ResponseEntity<>(createCustomerUC.handle(request), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
}
