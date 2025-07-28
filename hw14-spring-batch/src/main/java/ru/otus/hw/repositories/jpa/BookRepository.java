package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = "book-authors-genres-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Book> findById(long id);

    @EntityGraph(value = "book-authors-genres-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Book> findAll();

}
