package ru.gosarcho.affairs_query.controller;

import org.springframework.stereotype.Controller;
import ru.gosarcho.affairs_query.model.PersonModel;
import ru.gosarcho.affairs_query.service.AffairService;

import java.io.File;
import java.util.HashMap;

@Controller
class MainController {
    static final String READING_ROOM_DIRECTORY = "I:\\Читальный зал\\";
    static final String AFFAIRS_FOLDER_DIRECTORY = "I:\\Оцифровка\\Фонды\\";
    static final File AFFAIRS_FROM_DIGITIZER_1 = new File("I:\\Оцифровка\\КолесниковаЕ\\");
    static final File AFFAIRS_FROM_DIGITIZER_2 = new File("I:\\Оцифровка\\Степаненко\\");
    static final File AFFAIRS_FROM_DIGITIZER_3 = new File("I:\\Оцифровка\\Гимодудинов\\");

    static HashMap<String, PersonModel> persons = new HashMap<>();

    static AffairService affairService;

    public MainController(AffairService affairService) {
        MainController.affairService = affairService;
    }
}