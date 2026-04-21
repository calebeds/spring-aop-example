package me.calebeoliveira.springaopexample.mapper;

import me.calebeoliveira.springaopexample.entity.Customer;
import me.calebeoliveira.springaopexample.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toCustomer(CustomerDTO customerDTO);
    CustomerDTO toCustomerDTO(Customer customer);
}
