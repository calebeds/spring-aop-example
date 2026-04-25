package me.calebeoliveira.springaopexample.repository;

import me.calebeoliveira.springaopexample.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
