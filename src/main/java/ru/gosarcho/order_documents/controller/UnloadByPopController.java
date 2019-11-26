package ru.gosarcho.order_documents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarcho.order_documents.entity.Document;
import ru.gosarcho.order_documents.util.DocumentsFilter;

import javax.lang.model.element.Element;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.gosarcho.order_documents.controller.MainController.documentService;
import static ru.gosarcho.order_documents.controller.MainController.filters;


@Controller
@RequestMapping("/unloadByPop")
public class UnloadByPopController {

    @RequestMapping(method = RequestMethod.GET)
    public String unloadFromDb(Model model) {
        List<Document> documentsFromDb = documentService.getAll();
        model.addAttribute("documentsFilter", new DocumentsFilter());
        return unloadSetAttributes(model, documentsFromDb);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String unloadFromDbWithFilter(Model model, @ModelAttribute("documentsFilter") DocumentsFilter documentsFilter, HttpSession session) {
        filters.put(session.getId(), documentsFilter);
        DocumentsFilter filter = filters.get(session.getId());
        if (filter.getDateFrom().length() == 0)
            filter.setDateFrom("2019-11-01");
        if (filter.getDateTo().length() == 0)
            filter.setDateTo(LocalDate.now().toString());
        filter.setReader("");
        filter.setExecutor("");
        List<Document> documentsFromDb = documentService.getAllByFilter(filter);
        return unloadSetAttributes(model, documentsFromDb);
    }

    private String unloadSetAttributes(Model model, List<Document> documentsFromDb) {
        ConcurrentHashMap<String, AtomicInteger> documentsByPop = new ConcurrentHashMap<>();
        for (Document document : documentsFromDb) {
            String doc = document.getFond() + ' ' + document.getOp() + ' ' + document.getDocument();
            documentsByPop.putIfAbsent(doc, new AtomicInteger(0));
            documentsByPop.get(doc).incrementAndGet();
        }
        model.addAttribute("documentsByPop", documentsByPop);
        model.addAttribute("documentsCount", "Всего дел: " + documentsByPop.size());
        return "unloadByPop";
    }
}