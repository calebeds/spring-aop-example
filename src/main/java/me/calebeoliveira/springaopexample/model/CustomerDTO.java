package me.calebeoliveira.springaopexample.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.calebeoliveira.springaopexample.client.dto.OrderDTO;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public final class CustomerDTO {
    @Null(message = "ID must be null for new customers")
    private Long id;
    @NotBlank(message = "Customer name is required")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;
    @Null(message = "email must be null for new customers")
    private String email;
    @Null(message = "phone must be null for new customers")
    private String phone;
    @Null(message = "age must be null for new customers")
    private Integer age;

    private List<OrderDTO> orders;
}
