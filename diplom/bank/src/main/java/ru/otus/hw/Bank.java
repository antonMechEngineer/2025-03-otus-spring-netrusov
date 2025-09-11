//todo: добавить схемы в родительский модуль и генерировать при помощи json2pojo
//todo: bean validation
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
