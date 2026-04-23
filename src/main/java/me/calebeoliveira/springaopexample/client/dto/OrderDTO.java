package me.calebeoliveira.springaopexample.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private Long customerId;
    private String product;
    private Double amount;
    private String status;
    private Instant orderDate;
}