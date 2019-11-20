package ru.gosarcho.order_documents.controller;

import org.springframework.stereotype.Controller;
import ru.gosarcho.order_documents.model.PersonModel;
import ru.gosarcho.order_documents.service.DocumentService;

import java.io.File;
import java.util.HashMap;

@Controller
class MainController {
    static final String READING_ROOM_DIRECTORY = "I:\\Читальный зал\\";
    static final String DOCUMENTS_FOLDER_DIRECTORY = "I:\\Оцифровка\\Фонды\\";
    static final File DOCUMENTS_FROM_DIGITIZER_1 = new File("I:\\Оцифровка\\КолесниковаЕ\\");
    static final File DOCUMENTS_FROM_DIGITIZER_2 = new File("I:\\Оцифровка\\Степаненко\\");
    static final File DOCUMENTS_FROM_DIGITIZER_3 = new File("I:\\Оцифровка\\Гимодудинов\\");

    static HashMap<String, PersonModel> persons = new HashMap<>();

    static DocumentService documentService;

    public MainController(DocumentService documentService) {
        MainController.documentService = documentService;
    }
}