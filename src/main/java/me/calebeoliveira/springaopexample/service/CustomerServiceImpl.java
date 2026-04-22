package me.calebeoliveira.springaopexample.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calebeoliveira.springaopexample.client.dto.CustomerResponse;
import me.calebeoliveira.springaopexample.client.dto.JsonServerClient;
import me.calebeoliveira.springaopexample.mapper.CustomerMapper;
import me.calebeoliveira.springaopexample.entity.Customer;
import me.calebeoliveira.springaopexample.model.CustomerDTO;
import me.calebeoliveira.springaopexample.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final JsonServerClient jsonServerClient;

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        customerRepository.save(customerMapper.toCustomer(customerDTO));
    }

    @Override
    public CustomerDTO getCustomer(long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        Customer customer = customerOptional.orElseThrow(IllegalArgumentException::new);


        CustomerDTO customerDTO = customerMapper.toCustomerDTO(customer);

        enrichCustomerDTO(customerDTO);

        return customerDTO;
    }

    private void enrichCustomerDTO(CustomerDTO customerDTO) {
        final Long id = customerDTO.getId();

        log.info("Fetching enrichment data for customer {} from external API", id);

        ResponseEntity<CustomerResponse> response = jsonServerClient.getCustomerById(id);

        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            CustomerResponse customerResponse = response.getBody();

            customerDTO.setAge(customerResponse.getAge());
            customerDTO.setPhone(customerResponse.getPhone());
            customerDTO.setEmail(customerResponse.getEmail());
        } else {
            log.warn("External API returned non-2xx status: {}", response.getStatusCode());
            setFallBackData(customerDTO);
        }
    }

    private void setFallBackData(CustomerDTO customerDTO) {
        customerDTO.setAge(0);
        customerDTO.setPhone("N/A");
        customerDTO.setEmail("N/A");
    }
}
