package dao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestServiceImpl;

class CsvQuestionDaoTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll(){

    }

}
