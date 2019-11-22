package ru.gosarcho.order_documents.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gosarcho.order_documents.entity.Document;

import javax.servlet.http.HttpServletResponse;

import static ru.gosarcho.order_documents.controller.MainController.documentService;
import static ru.gosarcho.order_documents.controller.UnloadController.unloadFilter;

@Controller
@RequestMapping("/exportCSV")
public class ExportDocsController {
    @RequestMapping(method = RequestMethod.GET)
    public void exportCSV(HttpServletResponse response) throws Exception {
        String filename = "documents.csv";
        response.setContentType("text/csv; charset=cp1251");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        StatefulBeanToCsv<Document> writer = new StatefulBeanToCsvBuilder<Document>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(documentService.getAllByFilter(unloadFilter));

    }
}
