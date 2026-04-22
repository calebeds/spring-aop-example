package me.calebeoliveira.springaopexample.service;

import me.calebeoliveira.springaopexample.client.dto.CustomerResponse;
import me.calebeoliveira.springaopexample.client.dto.OrderDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CustomerEnrichmentAsyncService {
    CompletableFuture<CustomerResponse> fetchCustomerEnrichmentAsync(Long id);

    CompletableFuture<List<OrderDTO>> fetchOrdersAsync(Long id);
}
