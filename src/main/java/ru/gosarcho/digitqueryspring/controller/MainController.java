package ru.gosarcho.digitqueryspring.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.gosarcho.digitqueryspring.entity.Affair;
import ru.gosarcho.digitqueryspring.form.AffairForm;
import ru.gosarcho.digitqueryspring.form.PersonForm;
import ru.gosarcho.digitqueryspring.model.AffairModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.gosarcho.digitqueryspring.model.PersonModel;
import ru.gosarcho.digitqueryspring.service.AffairService;

@Controller
public class MainController {

    private static List<AffairModel> affairModelList = new ArrayList<>();
    private static PersonModel person;
    @Value("${error.message}")
    private String errorMessage;

    @Autowired
    private AffairService affairService;

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        PersonForm personForm = new PersonForm();
        model.addAttribute("personForm", personForm);
        return "index";
    }

    @PostMapping({"/", "/index"})
    public String newPerson(Model model, @ModelAttribute("personForm") PersonForm personForm) {
        String lastName = personForm.getReaderLastName();
        String firstName = personForm.getReaderFirstName();
        String executor = personForm.getExecutorLastName();

        if (lastName != null && lastName.length() > 0
                && firstName != null && firstName.length() > 0
                && executor != null && executor.length() > 0) {
            person = new PersonModel(lastName + ' ' + firstName, executor);
            return "redirect:/affairsList";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "index";
    }

    @GetMapping("/affairsList")
    public String affairList(Model model) {
        model.addAttribute("person", person);
        model.addAttribute("affairs", affairModelList);
        if (affairModelList.size() != 0) {
            model.addAttribute("isSending", true);
        }
        return "affairsList";
    }

    @PostMapping("/affairsList")
    public String saveAndLoadAffairList() {
        for (AffairModel affairObj : affairModelList) {
            Affair affair = new Affair();
            affair.setFond(affairObj.getFond());
            affair.setOp(affairObj.getOp());
            affair.setAffair(affairObj.getAffair());
            affair.setPerson(person.getReaderFullName());
            affair.setExecutor(person.getExecutorLastName());
            affair.setReceiptDate(LocalDate.now());
            affairService.save(affair);
        }
        affairModelList.clear();
        return "load";
    }

    @GetMapping("/addAffair")
    public String showAddAffairPage(Model model) {
        model.addAttribute("affairForm", new AffairForm());
        return "addAffairs";
    }

    @PostMapping("/addAffair")
    public String saveAffair(Model model, @ModelAttribute("affairForm") AffairForm affairForm) {

        String fond = affairForm.getFond();
        String op = affairForm.getOp();
        String affair = affairForm.getAffair();

        if (fond != null && fond.length() > 0
                && op != null && op.length() > 0
                && affair != null && affair.length() > 0) {
            affairModelList.add(new AffairModel(fond, op, affair));

            return "redirect:/affairsList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addAffairs";
    }

}

