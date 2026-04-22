package me.calebeoliveira.springaopexample.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calebeoliveira.springaopexample.client.dto.CustomerResponse;
import me.calebeoliveira.springaopexample.client.dto.OrderDTO;
import me.calebeoliveira.springaopexample.mapper.CustomerMapper;
import me.calebeoliveira.springaopexample.entity.Customer;
import me.calebeoliveira.springaopexample.model.CustomerDTO;
import me.calebeoliveira.springaopexample.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerEnrichmentAsyncService customerEnrichmentAsyncService;

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        customerRepository.save(customerMapper.toCustomer(customerDTO));
    }

    @Override
    public CustomerDTO getCustomer(final long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        Customer customer = customerOptional.orElseThrow(IllegalArgumentException::new);

        CustomerDTO customerDTO = customerMapper.toCustomerDTO(customer);

        CompletableFuture<CustomerResponse> customerFuture = customerEnrichmentAsyncService.fetchCustomerEnrichmentAsync(id);
        CompletableFuture<List<OrderDTO>> ordersFuture = customerEnrichmentAsyncService.fetchOrdersAsync(id);

        CompletableFuture.allOf(customerFuture, ordersFuture).join();

        customerFuture.thenAccept(customerResponse -> {
            if(customerResponse != null) {
                applyEnrichment(customerDTO, customerResponse);
            } else {
                setFallBackData(customerDTO);
            }
        });

        ordersFuture.thenAccept(orders -> {
            customerDTO.setOrders(orders != null ? orders : Collections.emptyList());
        });

        return customerDTO;
    }

    private void applyEnrichment(CustomerDTO customerDTO, CustomerResponse customerResponse) {
        customerDTO.setAge(customerResponse.getAge());
        customerDTO.setPhone(customerResponse.getPhone());
        customerDTO.setEmail(customerResponse.getEmail());
    }

    private void setFallBackData(CustomerDTO customerDTO) {
        customerDTO.setAge(0);
        customerDTO.setPhone("N/A");
        customerDTO.setEmail("N/A");
    }
}
