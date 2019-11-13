package ru.gosarcho.digitqueryspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarcho.digitqueryspring.form.PersonForm;
import ru.gosarcho.digitqueryspring.model.PersonModel;

import static ru.gosarcho.digitqueryspring.controller.MainController.*;

@Controller
@RequestMapping({"/", "index"})
public class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        PersonForm personForm = new PersonForm();
        model.addAttribute("personForm", personForm);
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
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
        model.addAttribute("errorMessage", "Все поля должны быть заполнены");
        return "index";

    }
}