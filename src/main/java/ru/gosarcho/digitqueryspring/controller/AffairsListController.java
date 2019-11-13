package ru.gosarcho.digitqueryspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gosarcho.digitqueryspring.entity.Affair;
import ru.gosarcho.digitqueryspring.model.AffairModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static ru.gosarcho.digitqueryspring.controller.MainController.*;

@Controller
@RequestMapping("/affairsList")
public class AffairsListController {

    @RequestMapping(method = RequestMethod.GET)
    public String affairList(Model model) {
        model.addAttribute("person", person);
        model.addAttribute("affairs", affairModels);
        if (affairModels.size() != 0) {
            model.addAttribute("isSending", true);
        }
        return "affairsList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveAndLoadAffairList() {
        //Отправка файлов
        for (File affair : affairFiles) {
            try {
                String[] cutName = affair.getName().split("_");
                File out = new File(READING_ROOM_DIRECTORY + person.getReaderFullName()
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
        for (AffairModel affairModel : affairModels) {
            Affair affair = new Affair();
            affair.setFond(affairModel.getFond());
            affair.setOp(affairModel.getOp());
            affair.setAffair(affairModel.getAffair());
            affair.setReader(person.getReaderFullName());
            affair.setExecutor(person.getExecutorLastName());
            affair.setReceiptDate(LocalDate.now());
            affairService.save(affair);
        }
        affairModels.clear();
        return "load";
    }
}