package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

@DisplayName("Репозиторий комментариев")
@DataJpaTest
@Import(JpaCommentRepository.class)
public class JpaCommentRepositoryTest {
    private static final long FIRST_COMMENT_ID = 1L;
    private static final long SECOND_BOOK_ID = 2L;

    @Autowired
    private JpaCommentRepository jpaAuthorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("загрузка автора по id")
    @Test
    void findCommentById(){
        Comment actualComment = jpaAuthorRepository.findById(FIRST_COMMENT_ID).orElseThrow();
        Comment expectedComment = testEntityManager.find(Comment.class, FIRST_COMMENT_ID);
        Assertions.assertEquals(expectedComment, actualComment);
    }

    @DisplayName("загрузка всех комментариев к книге")
    @Test
    void findCommentByBook(){
        List<Comment> actualComments = jpaAuthorRepository.findByBook(SECOND_BOOK_ID);
        Book expectedBook = testEntityManager.find(Book.class, SECOND_BOOK_ID);
        List<Comment> expectedComments = testEntityManager.getEntityManager().createQuery("SELECT c FROM Comment c WHERE c.book = :book", Comment.class)
                .setParameter("book", expectedBook)
                .getResultList();
        Assertions.assertEquals(expectedComments, actualComments);
    }

}
