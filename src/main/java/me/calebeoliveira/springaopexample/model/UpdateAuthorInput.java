package me.calebeoliveira.springaopexample.model;

public record UpdateAuthorInput(
        Long id,
        String name,
        Integer birthYear,
        String nationality
) {
}
