package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CsvQuestionDao implements QuestionDao {
    private static final String READING_ERROR_DESCRIPTION = "Error file reading!";

    private static final int NUMBER_SKIP_LINES = 1;

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        InputStream inputStream = getClass().getResourceAsStream(fileNameProvider.getTestFileName());
        if (inputStream == null) {
            throw new QuestionReadException(READING_ERROR_DESCRIPTION);
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
            throw new QuestionReadException(READING_ERROR_DESCRIPTION, e);
        }
        return questions;

    }
}
