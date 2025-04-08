package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.hw.service.TestRunnerService;

public class Application {
    public static void main(String[] args) {

        //Прописать бины в spring-context.xml и создать контекст на его основе
        ApplicationContext context = new ClassPathXmlApplicationContext("C:\\Users\\anetr\\Desktop\\2025-03-otus-spring-netrusov\\hw01\\src\\main\\resources\\spring-context.xml");
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}