package ru.gosarcho.digitqueryspring.controller;

import org.springframework.stereotype.Controller;
import ru.gosarcho.digitqueryspring.model.AffairModel;
import ru.gosarcho.digitqueryspring.model.PersonModel;
import ru.gosarcho.digitqueryspring.service.AffairService;

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