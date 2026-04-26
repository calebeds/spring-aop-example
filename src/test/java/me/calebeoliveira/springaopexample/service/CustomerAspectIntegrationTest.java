package me.calebeoliveira.springaopexample.service;

import me.calebeoliveira.springaopexample.aspect.*;
import me.calebeoliveira.springaopexample.client.dto.CustomerResponse;
import me.calebeoliveira.springaopexample.entity.Customer;
import me.calebeoliveira.springaopexample.mapper.CustomerMapperImpl;
import me.calebeoliveira.springaopexample.repository.AuditLogRepository;
import me.calebeoliveira.springaopexample.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        CustomerServiceImpl.class,
        CustomerMapperImpl.class,
        CachingAspect.class,
        RetryAspectMyImpl.class,
        CircuitBreakerAspectMyImpl.class,
        RateLimitingAspect.class,
        PerformanceMonitoringAspect.class,
        AuditAspect.class
})
@EnableAspectJAutoProxy
public class CustomerAspectIntegrationTest {

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private CustomerEnrichmentAsyncService customerEnrichmentAsyncService;

    @MockitoBean
    private AuditLogRepository auditLogRepository;

    @Autowired
    private CustomerService customerService;

    @Test
    @DisplayName("@CacheResult should cache across different calls")
    void shouldCacheAcrossCalls() {
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);

        CustomerResponse mockEnrichment = CustomerResponse.builder()
                .age(30)
                .phone("123-4567")
                .email("john@example.com")
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(customerEnrichmentAsyncService.fetchCustomerEnrichmentAsync(1L))
                .thenReturn(CompletableFuture.completedFuture(mockEnrichment));
        when(customerEnrichmentAsyncService.fetchOrdersAsync(1L))
                .thenReturn(CompletableFuture.completedFuture(Collections.emptyList()));

        customerService.getCustomer(1L);
        customerService.getCustomer(1L);
        customerService.getCustomer(1L);

        verify(customerRepository, times(1)).findById(1L);
    }
}
