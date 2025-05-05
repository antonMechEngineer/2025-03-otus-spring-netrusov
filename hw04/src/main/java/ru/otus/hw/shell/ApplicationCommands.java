package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.LocalizedMessagesService;
import ru.otus.hw.service.TestRunnerService;


@ShellComponent(value = "Application Commands")
@RequiredArgsConstructor
public class ApplicationCommands {

    private final TestRunnerService testRunnerService;

    private final LocalizedMessagesService localizedMessagesService;

    @ShellMethod(value = "Run testing student", key = {"ts"})
    public String testStudent() {
        testRunnerService.run();
        return localizedMessagesService.getMessage("Testing.student.completed");
    }
}
