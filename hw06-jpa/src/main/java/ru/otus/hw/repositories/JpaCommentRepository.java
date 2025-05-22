package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    public JpaCommentRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Comment> findById(long id) {
        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c WHERE c.id = :id", Comment.class);
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Comment> findByBook(long bookId) {
        Book foundBook = em.find(Book.class, bookId);
        TypedQuery<Comment> queryComments =
                em.createQuery("SELECT c FROM Comment c WHERE c.book = :book", Comment.class);
        queryComments.setParameter("book", foundBook);
        return queryComments.getResultList();
    }
}
