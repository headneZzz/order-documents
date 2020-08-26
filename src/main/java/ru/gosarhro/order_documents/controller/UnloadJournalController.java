package ru.gosarhro.order_documents.controller;

import com.opencsv.CSVWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gosarhro.order_documents.util.DocumentsFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/unloadJournal")
public class UnloadJournalController {
    private List<String[]> digitDocsFromDb = new ArrayList<>();

    @RequestMapping(method = RequestMethod.GET)
    public String unloadFromDb(Model model) {
        connectToDbAndSetList(
                "SELECT сж.Номер, сж.ФОД, и.Исполнитель, сж.Дата_оцифровки, сж.Колво_файлов, сж.Колво_мб " +
                "FROM digitization.Сводный_журнал сж " +
                "INNER JOIN digitization.Исполнители и ON сж.Исполнитель = и.Код_исполнителя");
        model.addAttribute("documentsFilter", new DocumentsFilter());
        model.addAttribute("digitDocsFromDb", digitDocsFromDb);
        return "unloadJournal";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String unloadFromDbWithFilter(Model model, @ModelAttribute("documentsFilter") DocumentsFilter documentsFilter) {
        connectToDbAndSetList(
                "SELECT сж.Номер, сж.ФОД, и.Исполнитель, сж.Дата_оцифровки, сж.Колво_файлов, сж.Колво_мб " +
                        "FROM digitization.Сводный_журнал сж " +
                        "INNER JOIN digitization.Исполнители и ON сж.Исполнитель = и.Код_исполнителя " +
                        "WHERE (сж.Дата_оцифровки BETWEEN " + documentsFilter.getDateFrom() + " AND " + documentsFilter.getDateTo() + ") " +
                        "AND и.Исполнитель LIKE '%" + documentsFilter.getExecutor()+"%'");
        model.addAttribute("digitDocsFromDb", digitDocsFromDb);
        return "unloadJournal";
    }

    private void connectToDbAndSetList(String SQL) {
        digitDocsFromDb.clear();
        DecimalFormat form = new DecimalFormat("0.00");
        String url = "jdbc:postgresql://server:5433/archive";
        try (Connection connection = DriverManager.getConnection(url, "admin", "adminus")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                String[] names = resultSet.getString(2).split("_");
                String newName = "Ф." + names[0] + ". Оп." + names[1] + ". Д." + names[2];
                digitDocsFromDb.add(new String[]{
                        String.valueOf(resultSet.getInt(1)),
                        newName,
                        resultSet.getString(3),
                        String.valueOf(resultSet.getDate(4)),
                        String.valueOf(resultSet.getInt(5)),
                        form.format(resultSet.getDouble(6))});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/exportCSV")
    public void exportCsv(HttpServletResponse response) throws Exception {
        String filename = "journal.csv";
        response.setContentType("text/csv; charset=cp1251");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        CSVWriter writer = new CSVWriter(response.getWriter(), ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        String[] head = {"№ п/п", "Фонд, опись, дело", "Исполнитель", "Дата оцифровывания", "Кол-во файлов", "Кол-во Мб"};
        writer.writeNext(head);
        writer.writeAll(digitDocsFromDb);
    }

    @GetMapping("/refresh")
    public String refresh() {
        String url = "jdbc:postgresql://server:5433/archive";
        List<String> uniqueDocsFromFondsDb = new ArrayList<>();
        List<String> docsFromJournalDb = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, "admin", "adminus")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM digitization.Сводный_журнал");
            while (resultSet.next()) {
                docsFromJournalDb.add(resultSet.getString("ФОД"));
            }

            resultSet = statement.executeQuery("SELECT * FROM digitization.Оцифровка");
            int filesCount;
            double size;
            long sizeInBytes;
            while (resultSet.next()) {

//                if (resultSet.getString("Ссылка").charAt(0) != '#') {
//                    String[] temp = resultSet.getString("Ссылка").split("#");
//                    String fileNumber = resultSet.getString("Код_файла");
//                    statement.executeUpdate("update digitization.Оцифровка set Ссылка = '" + "#" + temp[0] + "#" + "' where Код_файла=" + fileNumber);
//                    resultSet = statement.executeQuery("SELECT * FROM digitization.Оцифровка");
//                    resultSet.next();
//                }

                String[] names = resultSet.getString("Имя_файла").split("_");
                String nameOfDoc = names[0] + '_' + names[1] + '_' + names[2];
                try {
                    if (!uniqueDocsFromFondsDb.contains(nameOfDoc) && !docsFromJournalDb.contains(nameOfDoc)) {
                        uniqueDocsFromFondsDb.add(nameOfDoc);
                        filesCount = 0;
                        sizeInBytes = 0;
                        File docInFonds = new File(resultSet.getString("Ссылка").substring(1));
                        for (File file : Objects.requireNonNull(docInFonds.getParentFile().listFiles())) {
                            sizeInBytes += file.length();
                            filesCount += 1;
                        }
                        size = (double) sizeInBytes / (1024 * 1024);
                        size = Math.round(size * 100.0) / 100.0;
                        PreparedStatement prst = connection.prepareStatement("INSERT INTO digitization.Сводный_журнал VALUES(DEFAULT,?,?,?,?,?)");
                        prst.setString(1, nameOfDoc);
                        prst.setInt(2, resultSet.getInt("Исполнитель"));
                        prst.setDate(3, resultSet.getDate("Дата_ввода"));
                        prst.setInt(4, filesCount);
                        prst.setDouble(5, size);
                        prst.executeUpdate();
                    }
                } catch (NullPointerException e) {
                    System.out.println(" - " + resultSet.getString("Имя_файла"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/unloadJournal";
    }
}