package ru.gosarhro.order_documents.controller;

import org.springframework.stereotype.Controller;
import ru.gosarhro.order_documents.model.SessionModel;
import ru.gosarhro.order_documents.service.DocumentService;
import ru.gosarhro.order_documents.util.DocumentsFilter;

import java.util.HashMap;

@Controller
class MainController {
    static HashMap<String, SessionModel> sessions = new HashMap<>();
    static HashMap<String, DocumentsFilter> filters = new HashMap<>();
    static DocumentService documentService;

    public MainController(DocumentService documentService) {
        MainController.documentService = documentService;
    }
}