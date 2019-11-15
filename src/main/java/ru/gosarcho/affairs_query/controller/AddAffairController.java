package ru.gosarcho.affairs_query.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarcho.affairs_query.form.AffairForm;
import ru.gosarcho.affairs_query.model.AffairModel;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.gosarcho.affairs_query.controller.MainController.*;

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

        if (fond != null && fond.length() > 0 && op != null && op.length() > 0 && affair != null && affair.length() > 0) {
            //Ищем дело в главной папке
            File affairDir = new File(AFFAIRS_FOLDER_DIRECTORY + fond + File.separator + op + File.separator + affair + File.separator);
            if (affairDir.exists()) {
                return addAndReturn(new AffairModel(fond, op, affair),Arrays.asList(Objects.requireNonNull(affairDir.listFiles())));
            }
            //Ищем дело в именных папках
            //папка№1
            File[] matchingAffairs = AFFAIRS_FROM_DIGITIZER_1.listFiles((dir, name) -> name.startsWith(fond+"_"+op+"_"+affair));
            if (matchingAffairs != null && matchingAffairs.length > 0) {
                return addAndReturn(new AffairModel(fond, op, affair), Arrays.asList(matchingAffairs));
            }

            //папка№2
            matchingAffairs = AFFAIRS_FROM_DIGITIZER_2.listFiles((dir, name) -> name.startsWith(fond+"_"+op+"_"+affair));
            if (matchingAffairs != null && matchingAffairs.length > 0){
                return addAndReturn(new AffairModel(fond,op,affair), Arrays.asList(matchingAffairs));
            }

            //папка№3
            matchingAffairs = AFFAIRS_FROM_DIGITIZER_3.listFiles((dir, name) -> name.startsWith(fond+"_"+op+"_"+affair));
            if (matchingAffairs != null && matchingAffairs.length > 0){
                return addAndReturn(new AffairModel(fond,op,affair), Arrays.asList(matchingAffairs));
            }

            model.addAttribute("errorMessage", "Данного дела нет в базе");
            return "addAffairs";
        }

        model.addAttribute("errorMessage", "Все поля должны быть заполнены");
        return "addAffairs";
    }
    private String addAndReturn(AffairModel affairModel, List<File> affairList) {
        affairModels.add(affairModel);
        affairFiles.addAll(affairList);
        return "redirect:/affairsList";
    }
}

//class AffairFilenameFilter implements FilenameFilter {
//
//    @Override
//    public boolean accept(File dir, String name) {
//        return name.startsWith(fond+"_"+op+"_"+affair) && name.endsWith(".jpg");
//    }
//}