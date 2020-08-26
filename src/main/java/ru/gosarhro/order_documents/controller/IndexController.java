package ru.gosarhro.order_documents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import ru.gosarhro.order_documents.form.PersonForm;
import ru.gosarhro.order_documents.model.SessionModel;

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
    public String newPerson(Model model, @ModelAttribute("personForm") PersonForm personForm, HttpSession session) {
        String reader = personForm.getReaderLastName().trim();
        String executor = personForm.getExecutorLastName().trim();
        if (reader.length() > 0  && executor.length() > 0) {
            MainController.sessions.put(session.getId(), new SessionModel(reader, executor));
            return "redirect:/addDocuments";
        }
        model.addAttribute("errorMessage", "Все поля должны быть заполнены");
        return "index";

    }
}