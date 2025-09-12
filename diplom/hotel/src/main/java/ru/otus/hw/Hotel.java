//todo: внедрить cash, rate-limiter на endpoints room
//todo: обработка случая когда положительная платежка есть, а заказа который она подтверждает нет или он отменен (построить работу с исключениями)
//todo: bean validation
//todo: построить работу с исключениями
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
