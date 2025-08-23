package ru.otus.hw.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "books", path = "books")
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = "book-authors-genres-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Book> findById(long id);

    @EntityGraph(value = "book-authors-genres-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Book> findAll();

    @Override
    @EntityGraph(value = "book-authors-genres-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    Page<Book> findAll(Pageable pageable);
}
