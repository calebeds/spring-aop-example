package me.calebeoliveira.springaopexample.service;

import lombok.RequiredArgsConstructor;
import me.calebeoliveira.springaopexample.mapper.CustomerMapper;
import me.calebeoliveira.springaopexample.entity.Customer;
import me.calebeoliveira.springaopexample.model.CustomerDTO;
import me.calebeoliveira.springaopexample.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        customerRepository.save(customerMapper.toCustomer(customerDTO));
    }

    @Override
    public CustomerDTO getCustomer(long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        Customer customer = customerOptional.orElseThrow(IllegalArgumentException::new);

        return customerMapper.toCustomerDTO(customer);
    }
}
