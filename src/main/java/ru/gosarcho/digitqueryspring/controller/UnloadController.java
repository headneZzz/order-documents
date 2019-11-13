package ru.gosarcho.digitqueryspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gosarcho.digitqueryspring.entity.Affair;
import ru.gosarcho.digitqueryspring.util.AffairFilter;

import java.time.LocalDate;
import java.util.List;

import static ru.gosarcho.digitqueryspring.controller.MainController.affairService;


@Controller
@RequestMapping("/unload")
public class UnloadController {

    @RequestMapping(method = RequestMethod.GET)
    public String unloadFromDb(Model model) {
        List<Affair> affairsFromDb = affairService.getAll();
        model.addAttribute("affairFilter", new AffairFilter());
        return unloadSetAttributes(model, affairsFromDb);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String unloadFromDbWithFilter(Model model, @ModelAttribute("affairFilter") AffairFilter affairFilter) {
        if (affairFilter.getDateFrom().length() == 0)
            affairFilter.setDateFrom("1960-01-01");
        if (affairFilter.getDateTo().length() == 0)
            affairFilter.setDateTo(LocalDate.now().toString());

        LocalDate dateFrom = LocalDate.parse(affairFilter.getDateFrom());
        LocalDate dateTo = LocalDate.parse(affairFilter.getDateTo());
        List<Affair> affairsFromDb = affairService.getAllByFilter(dateFrom, dateTo, affairFilter.getReader(), affairFilter.getExecutor());
        return unloadSetAttributes(model, affairsFromDb);
    }

    private String unloadSetAttributes(Model model, List<Affair> affairsFromDb) {
        model.addAttribute("affairs", affairsFromDb);
        model.addAttribute("affairsCount", "Всего дел: " + affairsFromDb.size());
        String[] affairsNames = affairsFromDb.stream().map(a -> a.getFond() + '_' + a.getOp() + '_' + a.getAffair()).distinct().toArray(String[]::new);
        model.addAttribute("uniqueAffairsCount", "Уникальных дел: " + affairsNames.length);
        return "unloadFromDb";
    }
}