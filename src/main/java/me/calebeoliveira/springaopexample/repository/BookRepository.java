package me.calebeoliveira.springaopexample.repository;

import me.calebeoliveira.springaopexample.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByGenre(String genre);

    List<Book> findByAuthorId(Long authorId);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> searchByTitle(@Param("title") String title);
}
