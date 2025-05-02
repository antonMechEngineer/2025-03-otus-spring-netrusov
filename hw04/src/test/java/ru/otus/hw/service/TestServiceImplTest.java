package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestServiceImplTest {

    @MockitoBean
    private LocalizedIOService ioService;
    @MockitoBean
    private  QuestionDao questionDao;
    @MockitoBean
    private  UserOutputMapper userOutputMapper;
    @Autowired
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
        when(ioService.readIntForRangeLocalized(anyInt(), anyInt(), anyString())).thenReturn(1);
        testService.executeTestFor(student);
        verify(questionDao, times(1)).findAll();
        verify(ioService, times(1)).readIntForRangeLocalized(anyInt(), anyInt(), anyString());
        verify(userOutputMapper, times(1)).mapQuestionToString(any());
    }
}