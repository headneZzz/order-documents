package ru.gosarcho.order_documents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gosarcho.order_documents.form.DocumentForm;
import ru.gosarcho.order_documents.model.DocumentModel;

import javax.servlet.http.HttpSession;

import static ru.gosarcho.order_documents.controller.AddDocumentController.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tryAnother")
public class TryAnotherController {
    @RequestMapping(method = RequestMethod.GET)
    public String tryAnother(Model model, HttpSession session) {
        model.addAttribute("documentForm", new DocumentForm());
        List<File> tempFileList = tempInit();
        for (File file : tempFileList) {
            List<File> matchingDocuments = new ArrayList<>();
            listForDocs(file,matchingDocuments);
            if (matchingDocuments.size() > 0) {
                return addAndReturn(new DocumentModel(document[0], document[1], document[2]), matchingDocuments, session.getId());
            }
        }
        model.addAttribute("errorMessage", "Данного дела нет в архивной базе.");
        return "redirect:/addDocument";
    }

    private List<File> tempInit() {
        List<File> tempFileList = new ArrayList<>();
        tempFileList.add(new File("I:\\Оцифровка\\FotoArh2\\"));

        tempFileList.add(new File("I:\\Оцифровка\\Аутсорсинг\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Аутсорсинг_БС_2018\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Аутсорсинг_БС_2019\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Аутсорсинг_Дими-Центр2019\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Аутсорсинг_Оптима\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Аутсорсинг_ЭЛАР\\"));

        tempFileList.add(new File("I:\\Оцифровка\\Гимодудинов_архив2\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Гимодудинов_архив3\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Гимодудинов_архив4\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Гимодудинов_архив5\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Гимодудинов_архив6\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Гимодудинов_архив7\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Гимодудинов_архив8\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Гимодудинов_архив9\\"));
        tempFileList.add(new File("I:\\Оцифровка\\Гимодудинов_архив10\\"));

        tempFileList.add(new File("I:\\Оцифровка\\КолесниковаЕ_архив\\"));
        tempFileList.add(new File("I:\\Оцифровка\\КолесниковаЕ_архив2\\"));

        return tempFileList;
    }
}
