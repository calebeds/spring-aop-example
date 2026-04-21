package me.calebeoliveira.springaopexample.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import me.calebeoliveira.springaopexample.model.CustomerDTO;
import me.calebeoliveira.springaopexample.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("id") @Positive(message = "ID must be positive") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomer(id));
    }

    @PostMapping
    void saveCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
        customerService.saveCustomer(customerDTO);
    }
}
