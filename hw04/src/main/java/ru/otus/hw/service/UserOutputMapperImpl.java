package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Question;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class UserOutputMapperImpl implements UserOutputMapper {

    private final LocalizedMessagesService localizedMessagesService;

    @Override
    public String mapQuestionToString(Question question) {
        String answers = IntStream.range(1, question.answers().size() + 1)
                .mapToObj(i ->
                        localizedMessagesService.getMessage("UserOutputMapper.Answer.variant",
                                i,
                                question.answers().get(i - 1).text(),
                                "\n"))
                .collect(Collectors.joining());
        return localizedMessagesService.getMessage("UserOutputMapper.question",
                question.text(),
                "\n",
                answers);
    }
}