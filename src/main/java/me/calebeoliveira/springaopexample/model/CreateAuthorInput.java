package me.calebeoliveira.springaopexample.model;

public record CreateAuthorInput(
        String name,
        Integer birthYear,
        String nationality,
        String biography
) {
}
