package ru.gosarcho.digitqueryspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO Подключить БД и реализовать функции выгрузки дел:
// - по частоте обращения к конкретному делу
// - для конкретного посетителя
// - по датам
//TODO Реализовать функцию отправки нужных дел
@SpringBootApplication
public class DigitqueryspringApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitqueryspringApplication.class, args);
    }

}
