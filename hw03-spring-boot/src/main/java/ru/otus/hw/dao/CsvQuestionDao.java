package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedMessagesService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {

    private static final int NUMBER_SKIP_LINES = 1;

    private final TestFileNameProvider fileNameProvider;

    private final LocalizedMessagesService localizedMessagesService;

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        String fileName = fileNameProvider.getTestFileName();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new QuestionReadException(localizedMessagesService.getMessage("CsvQuestionDao.error"));
        }
        try (Reader reader = new InputStreamReader(inputStream)) {
            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(NUMBER_SKIP_LINES)
                    .build();
            List<QuestionDto> questionDtos = csvToBean.parse();
            questionDtos.forEach(qd -> questions.add(qd.toDomainObject()));
        } catch (IOException e) {
            throw new QuestionReadException(localizedMessagesService.getMessage("CsvQuestionDao.error"), e);
        }
        return questions;
    }
}
