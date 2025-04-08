package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;
import static org.mockito.Mockito.*;

public class TestServiceImplTest {

    @Mock
    private IOService ioService;
    @Mock
    private  QuestionDao questionDao;
    @InjectMocks
    private TestServiceImpl testService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void positiveExecuteTest(){
        Question question = new Question("test", List.of(new Answer("ans", true)));
        when(questionDao.findAll()).thenReturn(List.of(question));
        testService.executeTest();
        verify(questionDao, times(1)).findAll();
        verify(ioService, times(1)).printLine("");
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below%n");
        verify(ioService, times(1)).printLine(question.toString());
    }
}
