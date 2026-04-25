package me.calebeoliveira.springaopexample.model;

public record UpdateBookInput(
        Long id,
        String title,
        Integer publishedYear,
        String genre,
        Double price
) {

}
