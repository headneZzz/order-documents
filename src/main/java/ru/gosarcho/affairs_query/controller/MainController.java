package ru.gosarcho.affairs_query.controller;

import org.springframework.stereotype.Controller;
import ru.gosarcho.affairs_query.model.AffairModel;
import ru.gosarcho.affairs_query.model.PersonModel;
import ru.gosarcho.affairs_query.service.AffairService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
class MainController {
    static PersonModel person;
    static final String AFFAIRS_FOLDER_DIRECTORY = "I:\\Оцифровка\\Фонды\\";
    static final File AFFAIRS_FROM_DIGITIZER_1 = new File("I:\\Оцифровка\\КолесниковаЕ\\");
    static final File AFFAIRS_FROM_DIGITIZER_2 = new File("I:\\Оцифровка\\Степаненко\\");
    static final File AFFAIRS_FROM_DIGITIZER_3 = new File("I:\\Оцифровка\\Гимодудинов\\");
    static final String READING_ROOM_DIRECTORY = "I:\\Читальный зал\\";

    static AffairService affairService;

    public MainController(AffairService affairService) {
        MainController.affairService = affairService;
    }
}