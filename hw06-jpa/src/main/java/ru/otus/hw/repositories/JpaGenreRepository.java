package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaGenreRepository implements GenreRepository {

    @PersistenceContext
    private final EntityManager em;

    public JpaGenreRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Genre> findAll() {
        return em.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        Genre genre = em.find(Genre.class, id);
        return Optional.ofNullable(genre);
    }
}
