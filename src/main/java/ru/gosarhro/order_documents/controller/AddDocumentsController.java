package ru.gosarhro.order_documents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarhro.order_documents.form.DocumentsForm;
import ru.gosarhro.order_documents.model.DocumentModel;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.gosarhro.order_documents.controller.MainController.*;

@Controller
@RequestMapping("/addDocuments")
public class AddDocumentsController {
    private List<File> folders;

    @RequestMapping(method = RequestMethod.GET)
    public String showAddDocumentPage(Model model, HttpSession session) {
        try {
            model.addAttribute("person", sessions.get(session.getId()).getReaderFullName());
            model.addAttribute("documentsForm", new DocumentsForm());
            folders = foldersInit();
        } catch (NullPointerException e) {
            if (sessions.get(session.getId()) != null)
                e.printStackTrace();
            return "redirect:/index";
        }
        return "addDocuments";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveDocument(Model model, @ModelAttribute("documentsForm") DocumentsForm documentsForm, HttpSession session) {
        model.addAttribute("person", sessions.get(session.getId()).getReaderFullName());
        List<String> documentsNotFound = new ArrayList<>();
        String[] documents = documentsForm.getDocuments().split("\n");
        for (String doc : documents) {
            String[] document = doc.trim().split(" ");
            if (document.length != 3) {
                model.addAttribute("errorMessage", "Неправильно расставлены пробелы в " + doc);
                return "addDocuments";
            }

            //Ищем дело в главной папке
            File documentsDir = new File("I:\\Оцифровка\\Фонды\\" + document[0] + File.separator + document[1] + File.separator + document[2]);
            if (documentsDir.exists()) {
                sessions.get(session.getId()).getDocumentModels().add(new DocumentModel(document[0], document[1], document[2]));
                sessions.get(session.getId()).getDocumentFiles().addAll(Arrays.asList(Objects.requireNonNull(documentsDir.listFiles())));
                continue;
            }

            //Ищем в именных папках
            List<File> matchingFiles = new ArrayList<>();
            boolean isFound = false;
            for (File folder : folders) {
                try {
                    searchFiles(folder, matchingFiles, document);
                } catch (NullPointerException e) {
                    System.out.println(folder);
                    System.out.println("Files: " + Objects.requireNonNull(folder.listFiles()).length);
                    e.printStackTrace();
                }
                if (matchingFiles.size() > 0) {
                    sessions.get(session.getId()).getDocumentModels().add(new DocumentModel(document[0], document[1], document[2]));
                    sessions.get(session.getId()).getDocumentFiles().addAll(matchingFiles);
                    isFound = true;
                    break;
                }
            }
            if (!isFound) documentsNotFound.add(doc);
        }
        if (documentsNotFound.size() > 0) {
            model.addAttribute("errorMessage", documentsNotFound + " нет в базе.");
            return "addDocuments";
        }
        return "redirect:/load";
    }

    private void searchFiles(File directory, List<File> matchingFiles, String[] document) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                if (file.getName().startsWith(document[0] + "_" + document[1] + "_" + document[2] + "_")) {
                    matchingFiles.add(file);
                }
            } else if (file.isDirectory()) {
                searchFiles(file, matchingFiles, document);
            }
        }
    }

    private List<File> foldersInit() {
        List<File> foldersList = new ArrayList<>();
        foldersList.add(new File("I:\\Оцифровка\\КолесниковаЕ\\"));
        foldersList.add(new File("I:\\Оцифровка\\Степаненко\\"));
        foldersList.add(new File("I:\\Оцифровка\\Гимодудинов\\"));
        foldersList.add(new File("I:\\Оцифровка\\Стасюкевич\\"));

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
}

//class AffairFilenameFilter implements FilenameFilter {
//    @Override
//    public boolean accept(File dir, String name) {
//        return name.startsWith(fond+"_"+op+"_"+affair);
//    }
//}