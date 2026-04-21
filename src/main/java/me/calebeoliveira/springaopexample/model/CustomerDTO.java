package me.calebeoliveira.springaopexample.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public record CustomerDTO(@Null(message = "ID must be null for new customers")
                          Long id,
                          @NotBlank(message = "Customer name is required")
                          @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
                          String name) {
}
