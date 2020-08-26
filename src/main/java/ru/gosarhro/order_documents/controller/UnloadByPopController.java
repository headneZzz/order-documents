package ru.gosarhro.order_documents.controller;

import com.opencsv.CSVWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarhro.order_documents.entity.Document;
import ru.gosarhro.order_documents.util.DocumentsFilter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.*;


@Controller
@RequestMapping("/unloadByPop")
public class UnloadByPopController {
    private Map<String, Integer> docsByPop;

    @RequestMapping(method = RequestMethod.GET)
    public String unloadFromDb(Model model) {
        List<Document> documentsFromDb = MainController.documentService.getAll();
        model.addAttribute("documentsFilter", new DocumentsFilter());
        return unloadSetAttributes(model, documentsFromDb);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String unloadFromDbWithFilter(Model model, @ModelAttribute("documentsFilter") DocumentsFilter documentsFilter, HttpSession session) {
        MainController.filters.put(session.getId(), documentsFilter);
        DocumentsFilter filter = MainController.filters.get(session.getId());
        if (filter.getDateFrom().length() == 0)
            filter.setDateFrom("2019-11-01");
        if (filter.getDateTo().length() == 0)
            filter.setDateTo(LocalDate.now().toString());
        filter.setReader("");
        filter.setExecutor("");
        List<Document> documentsFromDb = MainController.documentService.getAllByFilter(filter);
        return unloadSetAttributes(model, documentsFromDb);
    }

    private String unloadSetAttributes(Model model, List<Document> documentsFromDb) {
        Map<String,Integer> tempMap = new HashMap<>();
        for (Document document : documentsFromDb) {
            String doc = document.getFond() + ' ' + document.getOp() + ' ' + document.getDocument();
            tempMap.merge(doc, 1, Integer::sum);
        }

        //Sort by value
        docsByPop = tempMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        model.addAttribute("documentsByPop", docsByPop);
        model.addAttribute("documentsCount", "Всего дел: " + docsByPop.size());
        return "unloadByPop";
    }

    @GetMapping("/exportCSV")
    public void exportCsv(HttpServletResponse response) throws Exception {
        String filename = "documentsByPop.csv";
        response.setContentType("text/csv; charset=cp1251");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        CSVWriter writer = new CSVWriter(response.getWriter(), ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        String[] head = {"Дело", "Кол-во заказов"};
        writer.writeNext(head);

        List<Map.Entry> entryList = new ArrayList<>(docsByPop.entrySet());
        List<String[]> docs = new ArrayList<>();
        for (Map.Entry entry : entryList) {
            docs.add(entry.toString().split("="));
        }
        writer.writeAll(docs);
    }
}