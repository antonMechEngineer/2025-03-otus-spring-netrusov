package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.LocalizedMessagesService;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
class CsvQuestionDaoTest {

    @MockitoBean
    private TestFileNameProvider testFileNameProvider;

    @MockitoBean
    private LocalizedMessagesService localizedMessagesService;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    void testFindAll() {
        when(testFileNameProvider.getTestFileName()).thenReturn("questions.csv");
        List<Question> questions = csvQuestionDao.findAll();
        Assertions.assertFalse(questions.isEmpty());
    }
}
