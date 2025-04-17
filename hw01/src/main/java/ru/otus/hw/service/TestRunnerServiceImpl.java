package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final IOService ioService;

    @Override
    public void run() {
        try {
            testService.executeTest();
        } catch (QuestionReadException exception) {
            exception.printStackTrace();
            ioService.printLine("Ошибка чтения вопросов!");
        } catch (Throwable e) {
            ioService.printLine("Неизвестная ошибка при выполнении!");
        }
    }
}
