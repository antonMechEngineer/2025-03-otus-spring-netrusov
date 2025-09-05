//package ru.otus.hw.services;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//@DisplayName("Сервис комментариев")
//@DataJpaTest
//@Import({CommentServiceImpl.class})
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
//public class CommentServiceImplTest {
//
//    private static final long FIRST_COMMENT_ID = 1L;
//
//    @Autowired
//    private CommentService commentService;
//
//    @DisplayName("Проверка безопасности ленивого поля - книги")
//    @Test
//    void checkLazyFieldCommentGettingById() {
//        Comment comment = commentService.findById(FIRST_COMMENT_ID).orElseThrow();
//        Assertions.assertDoesNotThrow(comment::getBook);
//    }
//
//    @DisplayName("Проверка безопасности equals")
//    @Test
//    void checkEqualsCommentGettingById() {
//        Comment comment = commentService.findById(FIRST_COMMENT_ID).orElseThrow();
//        Assertions.assertDoesNotThrow(() -> comment.equals(comment));
//    }
//
//    @DisplayName("Проверка безопасности hashcode")
//    @Test
//    void checkHashCodeCommentGettingById() {
//        Comment comment = commentService.findById(FIRST_COMMENT_ID).orElseThrow();
//        Assertions.assertDoesNotThrow(comment::hashCode);
//    }
//
//    @DisplayName("Проверка безопасности toString")
//    @Test
//    void checkToStringCommentGettingById() {
//        Comment comment = commentService.findById(FIRST_COMMENT_ID).orElseThrow();
//        Assertions.assertDoesNotThrow(comment::toString);
//    }
//}
