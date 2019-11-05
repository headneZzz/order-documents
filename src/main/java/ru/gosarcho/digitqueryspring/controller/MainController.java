package ru.gosarcho.digitqueryspring.controller;

import java.util.ArrayList;
import java.util.List;

import ru.gosarcho.digitqueryspring.form.AffairForm;
import ru.gosarcho.digitqueryspring.form.PersonForm;
import ru.gosarcho.digitqueryspring.model.Affair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gosarcho.digitqueryspring.model.Person;


@Controller
public class MainController {

    private static List<Affair> affairs = new ArrayList<>();
    private static Person person;
    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        PersonForm personForm = new PersonForm();
        model.addAttribute("personForm", personForm);
        return "index";
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.POST)
    public String newPerson(Model model, @ModelAttribute("personForm") PersonForm personForm) {
        String lastName = personForm.getLastName();
        String firstName = personForm.getFirstName();
        String middleName = personForm.getMiddleName();

        if (lastName != null && lastName.length() > 0
                && firstName != null && firstName.length() > 0
                && middleName != null && middleName.length() > 0) {
            person = new Person(lastName, firstName, middleName);
            return "redirect:/affairsList";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "index";
    }

    @RequestMapping(value = {"/affairsList"}, method = RequestMethod.GET)
    public String affairList(Model model) {
        model.addAttribute("person", person);
        model.addAttribute("affairs", affairs);

        return "affairsList";
    }

    @RequestMapping(value = {"/addAffair"}, method = RequestMethod.GET)
    public String showAddAffairPage(Model model) {

        AffairForm affairForm = new AffairForm();
        model.addAttribute("affairForm", affairForm);

        return "addAffairs";
    }

    @RequestMapping(value = {"/addAffair"}, method = RequestMethod.POST)
    public String saveAffair(Model model, @ModelAttribute("affairForm") AffairForm affairForm) {

        String fund = affairForm.getFund();
        String register = affairForm.getRegister();
        String affair = affairForm.getAffair();

        if (fund != null && fund.length() > 0
                && register != null && register.length() > 0
                && affair != null && affair.length() > 0) {
            affairs.add(new Affair(fund, register, affair));

            return "redirect:/affairsList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addAffairs";
    }

}

