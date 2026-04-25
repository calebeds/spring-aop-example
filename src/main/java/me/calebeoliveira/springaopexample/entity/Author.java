package me.calebeoliveira.springaopexample.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(length = 50)
    private String nationality;

    @Column(length = 1000)
    private String biography;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();
}
