package me.calebeoliveira.springaopexample.service;

import me.calebeoliveira.springaopexample.model.CustomerDTO;

public interface CustomerService {
    void saveCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomer(long id);
}
