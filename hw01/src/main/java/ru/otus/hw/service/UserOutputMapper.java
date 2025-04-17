package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface UserOutputMapper {
    String mapQuestionToString(Question question);
}
