package ru.otus.hw.service;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Question;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class UserOutputMapperImpl implements UserOutputMapper {

    @Override
    public String mapQuestionToString(Question question) {
        String answers = IntStream.range(1, question.answers().size() + 1)
                .mapToObj(i ->
                        String.format("Answer variant № %d: %s\n", i, question.answers().get(i - 1).text()))
                .collect(Collectors.joining());
        return String.format("Question: %s\n%s", question.text(), answers);
    }
}