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
            ioService.printLine("Error reading of questions!");
        } catch (Throwable e) {
            ioService.printLine("Unknown error!");
        }
    }
}
