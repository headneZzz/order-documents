package ru.gosarcho.affairs_query.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gosarcho.affairs_query.entity.Affair;
import ru.gosarcho.affairs_query.model.AffairModel;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static ru.gosarcho.affairs_query.controller.MainController.*;

@Controller
@RequestMapping("/affairsList")
public class AffairsListController {
    @RequestMapping(method = RequestMethod.GET)
    public String affairList(Model model, HttpSession session) {
        model.addAttribute("person", persons.get(session.getId()).getReaderFullName());
        model.addAttribute("affairs", persons.get(session.getId()).getAffairModels());
        if (persons.get(session.getId()).getAffairModels().size() != 0) {
            model.addAttribute("isSending", true);
        }
        return "affairsList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveAndLoadAffairList(HttpSession session) {
        //Отправка файлов
        for (File affair : persons.get(session.getId()).getAffairFiles()) {
            try {
                String[] cutName = affair.getName().split("_");
                File out = new File(READING_ROOM_DIRECTORY + persons.get(session.getId()).getReaderFullName()
                        + File.separator + cutName[0] + "_" + cutName[1] + "_" + cutName[2]);
                out.mkdirs();
                Files.copy(affair.toPath(),
                        new File(out.toString() + File.separator + affair.getName()).toPath(),
                        REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Сохранение в бд
        for (AffairModel affairModel : persons.get(session.getId()).getAffairModels()) {
            Affair affair = new Affair();
            affair.setFond(affairModel.getFond());
            affair.setOp(affairModel.getOp());
            affair.setAffair(affairModel.getAffair());
            affair.setReader(persons.get(session.getId()).getReaderFullName());
            affair.setExecutor(persons.get(session.getId()).getExecutorLastName());
            affair.setReceiptDate(LocalDate.now());
            affairService.save(affair);
        }
        persons.get(session.getId()).getAffairModels().clear();
        persons.get(session.getId()).getAffairFiles().clear();
        persons.remove(session.getId());
        return "load";
    }
}