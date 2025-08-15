package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.mockito.Mockito.*;

public class TestServiceImplTest {

    @Mock
    private LocalizedIOService ioService;
    @Mock
    private  QuestionDao questionDao;
    @Mock
    private  UserOutputMapper userOutputMapper;
    @InjectMocks
    private TestServiceImpl testService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void positiveExecuteTest(){
        Question question = new Question("test", List.of(new Answer("ans", true)));
        Student student = new Student("a", "b");
        when(questionDao.findAll()).thenReturn(List.of(question));
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1);
        testService.executeTestFor(student);
        verify(questionDao, times(1)).findAll();
        verify(ioService, times(1)).readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(),  anyString());
        verify(userOutputMapper, times(1)).mapQuestionToString(any());
    }
}