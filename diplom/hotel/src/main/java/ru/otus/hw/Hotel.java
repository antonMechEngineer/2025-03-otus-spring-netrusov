//todo: добавить логгирование slf4j, особенно на проверки соотвтетсвия с аутентификацией в заказах
//todo: проверить отсутствие проблемы n+1
//todo: переписать тесты
//todo: сформировать корректный баннер
//todo: сделать сборку jar через докер
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
