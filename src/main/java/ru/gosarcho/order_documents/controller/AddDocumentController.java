package ru.gosarcho.order_documents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarcho.order_documents.form.DocumentForm;
import ru.gosarcho.order_documents.model.DocumentModel;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.gosarcho.order_documents.controller.MainController.*;

@Controller
@RequestMapping("/addDocument")
public class AddDocumentController {
    @RequestMapping(method = RequestMethod.GET)
    public String showAddDocumentPage(Model model) {
        model.addAttribute("documentForm", new DocumentForm());
        return "addDocument";
    }

    private String[] document;

    @RequestMapping(method = RequestMethod.POST)
    public String saveDocument(Model model, @ModelAttribute("documentForm") DocumentForm documentForm, HttpSession session) {
        document = documentForm.getDocument().split("[ \\-_]");
        if (document.length == 3) {
            //Ищем дело в главной папке
            File documentsDir = new File("I:\\Оцифровка\\Фонды" + File.separator + document[0] + File.separator + document[1] + File.separator + document[2]);
            if (documentsDir.exists()) {
                return addAndReturn(new DocumentModel(document[0], document[1], document[2]),
                        Arrays.asList(Objects.requireNonNull(documentsDir.listFiles())),
                        session.getId());
            }
            //Ищем в именных папках
            List<File> folders = foldersInit();
            List<File> matchingDocuments = new ArrayList<>();
            for (File folder : folders) {
                listForDocs(folder, matchingDocuments);
                if (matchingDocuments.size() > 0)
                    return addAndReturn(new DocumentModel(document[0], document[1], document[2]), matchingDocuments, session.getId());
            }
            model.addAttribute("errorMessage", "Данного дела нет в базе.");
        }
        model.addAttribute("errorMessage", "Все поля должны быть заполнены правильно");
        return "addDocument";
    }

    private void listForDocs(File directory, List<File> mf) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                if (file.getName().startsWith(document[0] + "_" + document[1] + "_" + document[2] + "_")) {
                    mf.add(file);
                }
            } else if (file.isDirectory()) {
                listForDocs(file, mf);
            }
        }
    }

    private List<File> foldersInit() {
        List<File> foldersList = new ArrayList<>();
        foldersList.add(new File("I:\\Оцифровка\\КолесниковаЕ\\"));
        foldersList.add(new File("I:\\Оцифровка\\Степаненко\\"));
        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов\\"));

        foldersList.add(new File("I:\\Оцифровка\\FotoArh2\\"));

        foldersList.add(new File("I:\\Оцифровка\\Аутсорсинг\\"));
        foldersList.add(new File("I:\\Оцифровка\\Аутсорсинг_БС_2018\\"));
        foldersList.add(new File("I:\\Оцифровка\\Аутсорсинг_БС_2019\\"));
        foldersList.add(new File("I:\\Оцифровка\\Аутсорсинг_Дими-Центр2019\\"));
        foldersList.add(new File("I:\\Оцифровка\\Аутсорсинг_Оптима\\"));
        foldersList.add(new File("I:\\Оцифровка\\Аутсорсинг_ЭЛАР\\"));

        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов_архив2\\"));
        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов_архив3\\"));
        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов_архив4\\"));
        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов_архив5\\"));
        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов_архив6\\"));
        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов_архив7\\"));
        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов_архив8\\"));
        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов_архив9\\"));
        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов_архив10\\"));

        foldersList.add(new File("I:\\Оцифровка\\КолесниковаЕ_архив\\"));
        foldersList.add(new File("I:\\Оцифровка\\КолесниковаЕ_архив2\\"));

        return foldersList;
    }

    private String addAndReturn(DocumentModel documentModel, List<File> documentsList, String sessionId) {
        sessions.get(sessionId).getDocumentModels().add(documentModel);
        sessions.get(sessionId).getDocumentFiles().addAll(documentsList);
        return "redirect:/documentsList";
    }
}

//class AffairFilenameFilter implements FilenameFilter {
//    @Override
//    public boolean accept(File dir, String name) {
//        return name.startsWith(fond+"_"+op+"_"+affair);
//    }
//}