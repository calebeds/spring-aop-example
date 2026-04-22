package me.calebeoliveira.springaopexample.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calebeoliveira.springaopexample.client.JsonServerClient;
import me.calebeoliveira.springaopexample.client.dto.CustomerResponse;
import me.calebeoliveira.springaopexample.client.dto.OrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomerEnrichmentAsyncServiceImpl implements CustomerEnrichmentAsyncService {
    private final JsonServerClient jsonServerClient;

    @Override
    @Async
    public CompletableFuture<CustomerResponse> fetchCustomerEnrichmentAsync(final Long id) {

        try {
            log.info("Starting enrichment fetch for customer {}", id);

            final ResponseEntity<CustomerResponse> customerResponse = jsonServerClient.getCustomerById(id);

            if(customerResponse.getStatusCode().is2xxSuccessful() && customerResponse.getBody() != null) {
                log.info("Enrichment fetch completed for customer {}", id);
                return CompletableFuture.completedFuture(customerResponse.getBody());
            }
        } catch (Exception e) {
            log.error("Error fetching for customer {}", id);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<OrderDTO>> fetchOrdersAsync(final Long id) {
        try {
            log.info("Starting orders fetch for customer {}", id);

            ResponseEntity<List<OrderDTO>> response = jsonServerClient.getOrdersByCustomerId(id);

            if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<OrderDTO> orders = response.getBody();
                log.info("Orders fetch completed for customer {}", id);
                return CompletableFuture.completedFuture(orders);
            }
        } catch (Exception e) {
            log.error("Error fetch orders for customer {}: {}", id, e.getMessage(), e);
        }

        return CompletableFuture.completedFuture(null);
    }
}
