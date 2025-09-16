//todo: rate-limiter на endpoints room
//todo: сделать rooms публичным endpoint, при нажатии на кнопку забронировать при отсутствии авторизации, сделать редирект на login
//todo: обработка случая когда положительная платежка есть, а заказа который она подтверждает нет или он отменен (построить работу с исключениями)
//todo: построить работу с исключениями
//todo: переписать тесты
//todo: написать swagger спецификацию
//todo: добавить админу страничку для создания, удаления, редактирования комнат(обработать исключение, например когда админ пытается внести изменения (удалить) в уже забронированную комнату)

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
