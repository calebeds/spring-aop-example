package me.calebeoliveira.springaopexample.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.calebeoliveira.springaopexample.entity.Author;
import me.calebeoliveira.springaopexample.entity.Book;
import me.calebeoliveira.springaopexample.model.CreateAuthorInput;
import me.calebeoliveira.springaopexample.model.CreateBookInput;
import me.calebeoliveira.springaopexample.model.UpdateAuthorInput;
import me.calebeoliveira.springaopexample.model.UpdateBookInput;
import me.calebeoliveira.springaopexample.repository.AuthorRepository;
import me.calebeoliveira.springaopexample.repository.BookRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookGraphQLController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @QueryMapping
    public List<Book> books() {
        log.info("GraphQL: Fetching all books");
        return bookRepository.findAll();
    }

    @QueryMapping
    public Book book(@Argument Long id) {
        log.info("GraphQL: Fetching book with id={}", id);
        return bookRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Book> booksByGenre(@Argument String genre) {
        log.info("GraphQL: Fetching books by genre={}", genre);
        return bookRepository.findByGenre(genre);
    }

    @QueryMapping
    public List<Book> booksByAuthor(@Argument Long authorId) {
        log.info("GraphQL: Fetching books for authorId={}", authorId);
        return bookRepository.findByAuthorId(authorId);
    }

    @QueryMapping
    public List<Author> authors() {
        log.info("GraphQL: Fetching all authors");
        return authorRepository.findAll();
    }

    @QueryMapping
    public List<Book> searchBooks(@Argument String title) {
        log.info("GraphQL: Searching books by title={}", title);
        return bookRepository.searchByTitle(title);
    }

    @MutationMapping
    public Book createBook(@Argument CreateBookInput input) {
        log.info("GraphQL: Creating book with input={}", input);

        Author author = authorRepository.findById(input.authorId())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + input.authorId()));

        Book book = Book.builder()
                .title(input.title())
                .author(author)
                .publishedYear(input.publishedYear())
                .genre(input.genre())
                .price(input.price())
                .isbn(input.isbn())
                .build();

        return bookRepository.save(book);
    }

    @MutationMapping
    public Book updateBook(@Argument UpdateBookInput input) {
        log.info("GraphQL: Updating book with input={}", input);

        Book book = bookRepository.findById(input.id())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + input.id()));

        if(input.title() != null) {
            book.setTitle(input.title());
        }

        if(input.publishedYear() != null) {
            book.setPublishedYear(input.publishedYear());
        }

        if(input.genre() != null) {
            book.setGenre(input.genre());
        }

        if(input.price() != null) {
            book.setPrice(input.price());
        }

        return bookRepository.save(book);
    }

    @MutationMapping
    public Boolean deleteBook(@Argument Long id) {
        log.info("GraphQL: Deleting book with id={}", id);

        if(!bookRepository.existsById(id)) {
            return false;
        }

        bookRepository.deleteById(id);

        return true;
    }

    @MutationMapping
    public Author createAuthor(@Argument CreateAuthorInput input) {
        log.info("GraphQL: Creating author with input={}", input);

        Author author = Author.builder()
                .name(input.name())
                .birthYear(input.birthYear())
                .nationality(input.nationality())
                .biography(input.biography())
                .build();

        return authorRepository.save(author);
    }

    @MutationMapping
    public Author updateAuthor(@Argument UpdateAuthorInput input) {
        log.info("GraphQL: Updating author with input={}", input);

        Author author = authorRepository.findById(input.id())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + input.id()));

        if(input.name() != null) {
            author.setName(input.name());
        }

        if(input.birthYear() != null) {
            author.setBirthYear(input.birthYear());
        }

        if(input.nationality() != null) {
            author.setNationality(input.nationality());
        }

        return authorRepository.save(author);
    }

    @MutationMapping
    public Boolean deleteAuthor(@Argument Long id) {
        log.info("GraphQL: Deleting author with id={}", id);

        if(!authorRepository.existsById(id)) {
            return false;
        }

        authorRepository.deleteById(id);

        return true;
    }

    @SchemaMapping(typeName = "Book", field = "author")
    public Author getAuthorForBook(Book book) {
        log.debug("GraphQL: Resolving author for book id={}", book.getId());
        return book.getAuthor();
    }

    @SchemaMapping(typeName = "Author", field = "books")
    public List<Book> getBooksForAuthor(Author author) {
        log.debug("GraphQL: Resolving books for author id={}", author.getId());
        return bookRepository.findByAuthorId(author.getId());
    }
}
