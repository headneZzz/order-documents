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
    static List<AffairModel> affairModels = new ArrayList<>();
    static List<File> affairFiles = new ArrayList<>();
    static PersonModel person;
    static final String AFFAIRS_FOLDER_DIRECTORY = "D:\\test\\";
    static final String READING_ROOM_DIRECTORY = "D:\\test\\";

    static AffairService affairService;

    public MainController(AffairService affairService) {
        MainController.affairService = affairService;
    }
}