package me.calebeoliveira.springaopexample.service;

import me.calebeoliveira.springaopexample.client.JsonServerClient;
import me.calebeoliveira.springaopexample.client.dto.CustomerResponse;
import me.calebeoliveira.springaopexample.client.dto.OrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerEnrichmentAsyncServiceImplTest {
    @Mock
    private JsonServerClient jsonServerClient;

    @InjectMocks
    private CustomerEnrichmentAsyncServiceImpl customerEnrichmentAsyncService;

    private CustomerResponse mockCustomerResponse;
    private List<OrderDTO> mockOrders;

    @BeforeEach
    void setUp() {
        mockCustomerResponse = CustomerResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .phone("44 44444 4444")
                .age(15)
                .build();

        OrderDTO orderDTO = OrderDTO.builder()
                .id(1L)
                .customerId(1L)
                .amount(29.99)
                .product("Laptop")
                .build();

        OrderDTO orderDTO2 = OrderDTO.builder()
                .id(1L)
                .customerId(1L)
                .amount(29.99)
                .product("Laptop")
                .build();

        mockOrders = Arrays.asList(orderDTO, orderDTO2);

    }

    @Nested
    @DisplayName("fetchCustomerEnrichmentAsync tests")
    class FetchCustomerEnrichmentTests {
        @Test
        @DisplayName("Should return customer enrichment when api succeeds")
        void shouldReturnCustomerEnrichmentWhenApiSucceeds() throws ExecutionException, InterruptedException {
            Long customerId = 1L;
            ResponseEntity<CustomerResponse> mockResponse = ResponseEntity.ok(mockCustomerResponse);

            when(jsonServerClient.getCustomerById(customerId)).thenReturn(mockResponse);

            CompletableFuture<CustomerResponse> future = customerEnrichmentAsyncService.fetchCustomerEnrichmentAsync(customerId);
            CustomerResponse result = future.get();

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getLastName()).isEqualTo("Doe");

            verify(jsonServerClient).getCustomerById(customerId);
        }

        @Test
        @DisplayName("Should return null when api returns non-2xx status")
        void shouldReturnNullWhenAPIReturnsNon2xxStatus() throws ExecutionException, InterruptedException {
            Long customerId = 1L;
            ResponseEntity<CustomerResponse> mockResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            when(jsonServerClient.getCustomerById(customerId)).thenReturn(mockResponse);

            CompletableFuture<CustomerResponse> future = customerEnrichmentAsyncService.fetchCustomerEnrichmentAsync(customerId);
            CustomerResponse result = future.get();

            assertThat(result).isNull();
            verify(jsonServerClient).getCustomerById(customerId);
        }
    }

}