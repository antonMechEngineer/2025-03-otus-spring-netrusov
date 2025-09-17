//todo: обработка случая когда положительная платежка есть, а заказа который она подтверждает нет или он отменен (построить работу с исключениями) (обработать исключение, например когда админ пытается внести изменения (удалить) в уже забронированную комнату)
//todo: переписать тесты
//todo: написать swagger спецификацию
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
