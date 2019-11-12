package ru.gosarcho.digitqueryspring.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;

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
import ru.gosarcho.digitqueryspring.util.AffairFilter;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Controller
public class MainController {

    private static List<AffairModel> affairModels = new ArrayList<>();
    private static List<File> affairFiles = new ArrayList<>();
    private static PersonModel person;
    private final String AFFAIRS_FOLDER_DIRECTORY = "D:\\test\\";
    private final String READING_ROOM_DIRECTORY = "D:\\test\\";
    @Value("Все поля должны быть заполнены")
    private String fieldErrorMessage;
    @Value("Данного дела нет в базе")
    private String toFindAffairErrorMessage;

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
        model.addAttribute("errorMessage", fieldErrorMessage);
        return "index";

    }

    @GetMapping("/affairsList")
    public String affairList(Model model) {
        model.addAttribute("person", person);
        model.addAttribute("affairs", affairModels);
        if (affairModels.size() != 0) {
            model.addAttribute("isSending", true);
        }
        return "affairsList";
    }

    @PostMapping("/affairsList")
    public String saveAndLoadAffairList() {
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

            File affairDir = new File(AFFAIRS_FOLDER_DIRECTORY + fond + File.separator
                    + op + File.separator
                    + affair + File.separator);
            if (affairDir.exists()) {
                affairModels.add(new AffairModel(fond, op, affair));
                affairFiles.addAll(Arrays.asList(Objects.requireNonNull(affairDir.listFiles())));
                return "redirect:/affairsList";
            }
            model.addAttribute("errorMessage", toFindAffairErrorMessage);
            return "addAffairs";
        }

        model.addAttribute("errorMessage", fieldErrorMessage);
        return "addAffairs";
    }

    @GetMapping("/unload")
    public String unloadFromDb(Model model) {
        List<Affair> affairsFromDb = affairService.getAll();
        model.addAttribute("affairFilter", new AffairFilter());
        model.addAttribute("affairs", affairsFromDb);
        model.addAttribute("affairsCount", "Всего дел: " + affairsFromDb.size());
        model.addAttribute("uniqueAffairsCount", "Уникальных дел: " + affairsFromDb.stream().map(affair -> affair.getFond()+affair.getOp()+affair.getAffair()).distinct().count());
        return "unloadFromDb";
    }

    @PostMapping("/unload")
    public String unloadFromDbWithFilter(Model model, @ModelAttribute("affairFilter") AffairFilter affairFilter) {
        if (affairFilter.getDateFrom().length() == 0)
            affairFilter.setDateFrom("1960-01-01");
        if (affairFilter.getDateTo().length() == 0)
            affairFilter.setDateTo(LocalDate.now().toString());

        LocalDate dateFrom = LocalDate.parse(affairFilter.getDateFrom());
        LocalDate dateTo = LocalDate.parse(affairFilter.getDateTo());
        List<Affair> affairsFromDb = affairService.getAllByFilter(dateFrom, dateTo, affairFilter.getReader(), affairFilter.getExecutor());
        model.addAttribute("affairs", affairsFromDb);
        model.addAttribute("affairsCount", "Всего дел: " + affairsFromDb.size());
        model.addAttribute("uniqueAffairsCount", "Уникальных дел: " + affairsFromDb.stream().distinct().count());
        return "unloadFromDb";
    }
}