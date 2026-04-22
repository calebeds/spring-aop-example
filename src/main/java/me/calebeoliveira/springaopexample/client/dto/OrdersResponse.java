package me.calebeoliveira.springaopexample.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersResponse {
    List<OrderDTO> orders;
}
