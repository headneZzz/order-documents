package ru.gosarcho.order_documents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import ru.gosarcho.order_documents.form.PersonForm;
import ru.gosarcho.order_documents.model.SessionModel;

import static ru.gosarcho.order_documents.controller.MainController.*;

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
        String reader = personForm.getReaderLastName();
        String executor = personForm.getExecutorLastName();
        if (reader != null && reader.length() > 0 && executor != null && executor.length() > 0) {
            sessions.put(session.getId(), new SessionModel(reader, executor));
            return "redirect:/documentsList";
        }
        model.addAttribute("errorMessage", "Все поля должны быть заполнены");
        return "index";

    }
}