package me.calebeoliveira.springaopexample.client.dto;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "jsonserver", url = "localhost:3000")
@Retry(name = "jsonserver")
@CircuitBreaker(name = "jsonserver")
public interface JsonServerClient {
    @GetMapping("/customers/{id}")
    ResponseEntity<CustomerResponse> getCustomerById(@PathVariable("id") Long id);

    default ResponseEntity<CustomerResponse> fallbackGetCustomerById(Long id, Throwable throwable) {
        CustomerResponse fallbackCustomer = CustomerResponse.builder()
                .id(id)
                .age(0)
                .firstName("unknown")
                .lastName("unknown")
                .email("unavailable@example.com")
                .phone("N/A")
                .build();

        return ResponseEntity.ok(fallbackCustomer);
    }
}
