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

    static String[] document;

    @RequestMapping(method = RequestMethod.POST)
    public String saveDocument(Model model, @ModelAttribute("documentForm") DocumentForm documentForm, HttpSession session) {
        document = documentForm.getDocument().split("[ \\-_]");
        if (document.length == 3) {
            List<File> folders = new ArrayList<>();
            folders.add(new File("I:\\Оцифровка\\Фонды\\"));
            folders.add(new File("I:\\Оцифровка\\КолесниковаЕ\\"));
            folders.add(new File("I:\\Оцифровка\\Степаненко\\"));
            folders.add(new File("I:\\Оцифровка\\Гимодудинов\\"));

            //Ищем дело в главной папке
            File documentsDir = new File(folders.get(0) + document[0] + File.separator + document[1] + File.separator + document[2]);
            if (documentsDir.exists()) {
                return addAndReturn(new DocumentModel(document[0], document[1], document[2]), Arrays.asList(Objects.requireNonNull(documentsDir.listFiles())), session.getId());
            }

            //Ищем в именных папках
            List<File> matchingDocuments = new ArrayList<>();
            for (File folder : folders) {
                listForDocs(folder, matchingDocuments);
                if (matchingDocuments.size() > 0)
                    return addAndReturn(new DocumentModel(document[0], document[1], document[2]), matchingDocuments, session.getId());
            }

            model.addAttribute("errorMessage", "Данного дела нет в основной базе. Можете поискать в архивах, если уверены, что дело оцифровано (неизвестно, сколько займет времени). Иначе перезагрузите страницу, нажав F5 и введите другое дело");
            model.addAttribute("tryAnother", true);
            return "addDocument";
        }
        model.addAttribute("errorMessage", "Все поля должны быть заполнены правильно");
        return "addDocument";
    }

    static void listForDocs(File directory, List<File> mf) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                mf.addAll(Arrays.asList(Objects.requireNonNull(directory.listFiles((dir, name) -> name.startsWith(document[0] + "_" + document[1] + "_" + document[2] + "_")))));
            } else if (file.isDirectory()) {
                listForDocs(file, mf);
            }
        }
    }

    static String addAndReturn(DocumentModel documentModel, List<File> documentsList, String sessionId) {
        persons.get(sessionId).getDocumentModels().add(documentModel);
        persons.get(sessionId).getDocumentFiles().addAll(documentsList);
        return "redirect:/documentsList";
    }
}

//class AffairFilenameFilter implements FilenameFilter {
//    @Override
//    public boolean accept(File dir, String name) {
//        return name.startsWith(fond+"_"+op+"_"+affair);
//    }
//}