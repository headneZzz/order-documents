package ru.gosarcho.order_documents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gosarcho.order_documents.entity.Document;
import ru.gosarcho.order_documents.model.DocumentModel;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static ru.gosarcho.order_documents.controller.MainController.*;

@Controller
@RequestMapping("/documentsList")
public class DocumentsListController {
    @RequestMapping(method = RequestMethod.GET)
    public String documentsList(Model model, HttpSession session) {
        model.addAttribute("person", sessions.get(session.getId()).getReaderFullName());
        model.addAttribute("documents", sessions.get(session.getId()).getDocumentModels());
        if (sessions.get(session.getId()).getDocumentModels().size() != 0) {
            model.addAttribute("isSending", true);
        }
        return "documentsList";
    }


    @RequestMapping(method = RequestMethod.POST)
    public String saveAndLoadDocumentsList(HttpSession session) {
        String readingRoom = "I:\\Читальный зал\\";
        //Отправка файлов
        for (File doc : sessions.get(session.getId()).getDocumentFiles()) {
            try {
                String[] cutName = doc.getName().split("_");
                File out = new File(readingRoom + sessions.get(session.getId()).getReaderFullName()
                        + File.separator + cutName[0] + "_" + cutName[1] + "_" + cutName[2]);
                out.mkdirs();
                Files.copy(doc.toPath(), new File(out.toString() + File.separator + doc.getName()).toPath(), REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Сохранение в бд
        for (DocumentModel documentModel : sessions.get(session.getId()).getDocumentModels()) {
            Document document = new Document();
            document.setFond(documentModel.getFond());
            document.setOp(documentModel.getOp());
            document.setDocument(documentModel.getDocument());
            document.setReader(sessions.get(session.getId()).getReaderFullName());
            document.setExecutor(sessions.get(session.getId()).getExecutorLastName());
            document.setReceiptDate(LocalDate.now());
            documentService.save(document);
        }
        sessions.get(session.getId()).getDocumentModels().clear();
        sessions.get(session.getId()).getDocumentFiles().clear();
        sessions.remove(session.getId());
        return "load";
    }
}