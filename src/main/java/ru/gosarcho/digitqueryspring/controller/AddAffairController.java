package ru.gosarcho.digitqueryspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarcho.digitqueryspring.form.AffairForm;
import ru.gosarcho.digitqueryspring.model.AffairModel;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static ru.gosarcho.digitqueryspring.controller.MainController.*;

@Controller
@RequestMapping("/addAffair")
public class AddAffairController {

    @RequestMapping(method = RequestMethod.GET)
    public String showAddAffairPage(Model model) {
        model.addAttribute("affairForm", new AffairForm());
        return "addAffairs";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveAffair(Model model, @ModelAttribute("affairForm") AffairForm affairForm) {
        String fond = affairForm.getFond();
        String op = affairForm.getOp();
        String affair = affairForm.getAffair();

        if (fond != null && fond.length() > 0
                && op != null && op.length() > 0
                && affair != null && affair.length() > 0) {

            File affairDir = new File(AFFAIRS_FOLDER_DIRECTORY + fond + File.separator
                    + op + File.separator
                    + affair + File.separator);
            if (affairDir.exists()) {
                affairModels.add(new AffairModel(fond, op, affair));
                affairFiles.addAll(Arrays.asList(Objects.requireNonNull(affairDir.listFiles())));
                return "redirect:/affairsList";
            }
            model.addAttribute("errorMessage", "Данного дела нет в базе");
            return "addAffairs";
        }

        model.addAttribute("errorMessage", "Все поля должны быть заполнены");
        return "addAffairs";
    }
}