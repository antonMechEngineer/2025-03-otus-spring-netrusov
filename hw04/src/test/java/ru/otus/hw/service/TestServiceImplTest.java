package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = TestServiceImpl.class)
public class TestServiceImplTest {

    @MockitoBean
    private LocalizedIOService ioService;
    @MockitoBean
    private QuestionDao questionDao;
    @MockitoBean
    private UserOutputMapper userOutputMapper;
    @Autowired
    private TestServiceImpl testService;

    @Test
    void positiveExecuteTest() {
        Question question = new Question("test", List.of(new Answer("ans", true)));
        Student student = new Student("a", "b");
        when(questionDao.findAll()).thenReturn(List.of(question));
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1);
        testService.executeTestFor(student);
        verify(questionDao, times(1)).findAll();
        verify(ioService, times(1)).readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString());
        verify(userOutputMapper, times(1)).mapQuestionToString(any());
    }
}