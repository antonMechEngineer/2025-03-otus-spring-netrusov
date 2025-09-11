//todo: bean validation
//todo: сделать контракты через json2pojo
//todo: построить работу с исключениями
//todo: добавить логгирование slf4j, особенно на проверки соотвтетсвия с аутентификацией в заказах
//todo: проверить отсутствие проблемы n+1
//todo: переписать тесты
//todo: на endpoint комнат повесить rate limiter, а на сервисном уровне сделать cache

package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Hotel {
	public static void main(String[] args) {
		SpringApplication.run(Hotel.class, args);
	}

}
