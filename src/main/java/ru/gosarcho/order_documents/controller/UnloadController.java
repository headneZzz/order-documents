package ru.gosarcho.order_documents.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarcho.order_documents.entity.Document;
import ru.gosarcho.order_documents.util.DocumentsFilter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

import static ru.gosarcho.order_documents.controller.MainController.documentService;
import static ru.gosarcho.order_documents.controller.MainController.filters;


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
    public String unloadFromDbWithFilter(Model model, @ModelAttribute("documentsFilter") DocumentsFilter documentsFilter, HttpSession session) {
        filters.put(session.getId(), documentsFilter);
        DocumentsFilter filter = filters.get(session.getId());
        if (filter.getDateFrom().length() == 0)
            filter.setDateFrom("2019-11-01");
        if (filter.getDateTo().length() == 0)
            filter.setDateTo(LocalDate.now().toString());

        List<Document> documentsFromDb = documentService.getAllByFilter(filter);
        return unloadSetAttributes(model, documentsFromDb);
    }

    private String unloadSetAttributes(Model model, List<Document> documentsFromDb) {
        model.addAttribute("documents", documentsFromDb);
        model.addAttribute("documentsCount", "Всего дел: " + documentsFromDb.size());
        String[] documentsNames = documentsFromDb.stream().map(a -> a.getFond() + '_' + a.getOp() + '_' + a.getDocument()).distinct().toArray(String[]::new);
        model.addAttribute("uniqueDocumentsCount", "Уникальных дел: " + documentsNames.length);
        return "unload";
    }

    @GetMapping("/exportCSV")
    public void exportCsv(HttpServletResponse response, HttpSession session) throws Exception {
        String filename = "documents.csv";
        response.setContentType("text/csv; charset=cp1251");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        ColumnPositionMappingStrategy<Document> mappingStrategy = new ColumnPositionMappingStrategy<>();
        mappingStrategy.setType(Document.class);
        String[] columns = new String[]{"fond", "op", "document", "reader", "executor", "receiptDate"};
        mappingStrategy.setColumnMapping(columns);
        StatefulBeanToCsv<Document> writer = new StatefulBeanToCsvBuilder<Document>(response.getWriter())
                .withMappingStrategy(mappingStrategy)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(';')
                .withOrderedResults(false)
                .build();
        Document head = new Document(0L, "Фонд", "Опись", "Дело", "Читатель", "Исполнитель", null);
        writer.write(head);
        writer.write(documentService.getAllByFilter(filters.get(session.getId())));
    }
}