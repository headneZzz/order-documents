package ru.gosarcho.order_documents.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gosarcho.order_documents.entity.Document;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;

import static ru.gosarcho.order_documents.controller.MainController.documentService;
import static ru.gosarcho.order_documents.controller.MainController.filters;

@Controller
@RequestMapping("/exportCSV")
public class ExportCsvController {

    @RequestMapping(method = RequestMethod.GET)
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
        Document head = new Document(0L,"Фонд","Опись","Дело","Читатель","Исполнитель", null);
        writer.write(head);
        writer.write(documentService.getAllByFilter(filters.get(session.getId())));
    }
}
