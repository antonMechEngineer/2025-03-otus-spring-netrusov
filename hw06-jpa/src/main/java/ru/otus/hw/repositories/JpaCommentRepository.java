package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
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
        Comment comment = em.find(Comment.class, id);
        return Optional.ofNullable(comment);
    }

    @Override
    public List<Comment> findByBook(long bookId) {
        Book foundBook = em.find(Book.class, bookId);
        TypedQuery<Comment> queryComments =
                em.createQuery("SELECT c FROM Comment c WHERE c.book = :book", Comment.class);
        queryComments.setParameter("book", foundBook);
        return queryComments.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        Comment comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
        }
    }
}
