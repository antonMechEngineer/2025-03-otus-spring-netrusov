//todo: bean validation
//todo: отредактировать родительский pom ввести dependency management
//todo: построить работу с исключениями
//todo: проверить отсутствие проблемы n+1
//todo: переписать тесты
//todo: добавить логгирование slf4j, особенно на проверки соотвтетсвия с аутентификацией в заказах

package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Bank {

	public static void main(String[] args) {
		SpringApplication.run(Bank.class, args);
	}
}
