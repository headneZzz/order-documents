package ru.gosarhro.order_documents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.gosarhro.order_documents.entity.Document;
import ru.gosarhro.order_documents.model.DocumentModel;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Controller
public class SendDocumentsController {
    @GetMapping("/load")
    public String loadScreen() {
        return "load";
    }

    @GetMapping("/send")
    public String saveAndLoadDocumentsList(HttpSession session) {
        String readingRoom = "I:\\Читальный зал\\";
        //Отправка файлов
        for (File doc : MainController.sessions.get(session.getId()).getDocumentFiles()) {
            try {
                String[] cutName = doc.getName().split("_");
                File out = new File(readingRoom + MainController.sessions.get(session.getId()).getReaderFullName()
                        + File.separator + cutName[0] + "_" + cutName[1] + "_" + cutName[2]);
                out.mkdirs();
                Files.copy(doc.toPath(), new File(out.toString() + File.separator + doc.getName()).toPath(), REPLACE_EXISTING);
            } catch (IOException | ArrayIndexOutOfBoundsException | NullPointerException e) {
                System.out.println("THIS FILE NAME: " + doc.getName());
                System.out.println(MainController.sessions.get(session.getId()).getDocumentModels().toString());
                System.out.println("SIZE: " + MainController.sessions.get(session.getId()).getDocumentFiles().size());
                e.printStackTrace();
            }
        }
        //Сохранение в бд
        for (DocumentModel documentModel : MainController.sessions.get(session.getId()).getDocumentModels()) {
            Document document = new Document();
            document.setFond(documentModel.getFond());
            document.setOp(documentModel.getOp());
            document.setDocument(documentModel.getDocument());
            document.setReader(MainController.sessions.get(session.getId()).getReaderFullName());
            document.setExecutor(MainController.sessions.get(session.getId()).getExecutorLastName());
            document.setReceiptDate(LocalDate.now());
            MainController.documentService.save(document);
        }
        MainController.sessions.get(session.getId()).getDocumentModels().clear();
        MainController.sessions.get(session.getId()).getDocumentFiles().clear();
        MainController.sessions.remove(session.getId());
        return "redirect:/index";
    }
}