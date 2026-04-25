package me.calebeoliveira.springaopexample.model;

public record CreateBookInput(
        String title,
        Long authorId,
        Integer publishedYear,
        String genre,
        Double price,
        String isbn
) {

}
