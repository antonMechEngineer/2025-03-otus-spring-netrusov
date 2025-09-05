//todo: consumers не читают
//todo: проверить отсутствие проблемы n+1
//todo: переписать тесты

package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Bank {
	public static void main(String[] args) {
		SpringApplication.run(Bank.class, args);
	}

}
