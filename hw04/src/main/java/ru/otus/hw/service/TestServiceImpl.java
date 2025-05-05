package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final UserOutputMapper userOutputMapper;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        for (var question: questions) {
            ioService.printLine(userOutputMapper.mapQuestionToString(question));
            ioService.printFormattedLineLocalized("TestService.input.answer.variant");
            int currentAnswer = ioService.readIntForRangeLocalized(1, question.answers().size() + 1,
                    "TestService.invalid.answer.number");
            boolean isAnswerValid = question.answers().get(currentAnswer - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

}
