package ru.gosarcho.order_documents.controller;

import org.springframework.stereotype.Controller;
import ru.gosarcho.order_documents.model.PersonModel;
import ru.gosarcho.order_documents.service.DocumentService;

import java.util.HashMap;

@Controller
class MainController {
    static final String READING_ROOM_DIRECTORY = "I:\\Читальный зал\\";
    static HashMap<String, PersonModel> persons = new HashMap<>();
    static DocumentService documentService;
    public MainController(DocumentService documentService) {
        MainController.documentService = documentService;
    }
}