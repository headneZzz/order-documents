package ru.gosarcho.order_documents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarcho.order_documents.entity.Document;
import ru.gosarcho.order_documents.util.DocumentsFilter;

import java.time.LocalDate;
import java.util.List;

import static ru.gosarcho.order_documents.controller.MainController.documentService;


@Controller
@RequestMapping("/unload")
public class UnloadController {

    @RequestMapping(method = RequestMethod.GET)
    public String unloadFromDb(Model model) {
        List<Document> documentsFromDb = documentService.getAll();
        model.addAttribute("documentsFilter", new DocumentsFilter());
        return unloadSetAttributes(model, documentsFromDb);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String unloadFromDbWithFilter(Model model, @ModelAttribute("documentsFilter") DocumentsFilter documentsFilter) {
        if (documentsFilter.getDateFrom().length() == 0)
            documentsFilter.setDateFrom("1960-01-01");
        if (documentsFilter.getDateTo().length() == 0)
            documentsFilter.setDateTo(LocalDate.now().toString());

        LocalDate dateFrom = LocalDate.parse(documentsFilter.getDateFrom());
        LocalDate dateTo = LocalDate.parse(documentsFilter.getDateTo());
        List<Document> documentsFromDb = documentService.getAllByFilter(dateFrom, dateTo, documentsFilter.getReader(), documentsFilter.getExecutor());
        return unloadSetAttributes(model, documentsFromDb);
    }

    private String unloadSetAttributes(Model model, List<Document> documentsFromDb) {
        model.addAttribute("documents", documentsFromDb);
        model.addAttribute("documentsCount", "Всего дел: " + documentsFromDb.size());
        String[] documentsNames = documentsFromDb.stream().map(a -> a.getFond() + '_' + a.getOp() + '_' + a.getDocument()).distinct().toArray(String[]::new);
        model.addAttribute("uniqueDocumentsCount", "Уникальных дел: " + documentsNames.length);
        return "unloadFromDb";
    }
}