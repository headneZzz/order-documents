package ru.gosarcho.order_documents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarcho.order_documents.form.DocumentForm;
import ru.gosarcho.order_documents.model.DocumentModel;

import javax.servlet.http.HttpSession;
import java.io.File;
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

    @RequestMapping(method = RequestMethod.POST)
    public String saveDocument(Model model, @ModelAttribute("documentForm") DocumentForm documentForm, HttpSession session) {
        String []document = documentForm.getDocument().split("[ \\-_]");

        if (document[0] != null && document[0].length() > 0 && document[1] != null && document[1].length() > 0 && document[2] != null && document[2].length() > 0) {
            //Ищем дело в главной папке
            File documentsDir = new File(DOCUMENTS_FOLDER_DIRECTORY + document[0] + File.separator + document[1] + File.separator + document[2] + File.separator);
            if (documentsDir.exists()) {
                return addAndReturn(new DocumentModel(document[0], document[1], document[2]),Arrays.asList(Objects.requireNonNull(documentsDir.listFiles())),session.getId());
            }
            //Ищем дело в именных папках
            //папка№1
            File[] matchingDocuments = DOCUMENTS_FROM_DIGITIZER_1.listFiles((dir, name) -> name.startsWith(document[0]+"_"+document[1]+"_"+document[2]));
            if (matchingDocuments != null && matchingDocuments.length > 0) {
                return addAndReturn(new DocumentModel(document[0], document[1], document[2]), Arrays.asList(matchingDocuments),session.getId());
            }

            //папка№2
            matchingDocuments = DOCUMENTS_FROM_DIGITIZER_2.listFiles((dir, name) -> name.startsWith(document[0]+"_"+document[1]+"_"+document[2]));
            if (matchingDocuments != null && matchingDocuments.length > 0){
                return addAndReturn(new DocumentModel(document[0],document[1],document[2]), Arrays.asList(matchingDocuments), session.getId());
            }

            //папка№3
            matchingDocuments = DOCUMENTS_FROM_DIGITIZER_3.listFiles((dir, name) -> name.startsWith(document[0]+"_"+document[1]+"_"+document[2]));
            if (matchingDocuments != null && matchingDocuments.length > 0){
                return addAndReturn(new DocumentModel(document[0],document[1],document[2]), Arrays.asList(matchingDocuments), session.getId());
            }

            model.addAttribute("errorMessage", "Данного дела нет в базе");
            return "addDocument";
        }

        model.addAttribute("errorMessage", "Все поля должны быть заполнены");
        return "addDocument";
    }
    private String addAndReturn(DocumentModel documentModel, List<File> documentsList, String sessionId) {
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