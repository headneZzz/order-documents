package ru.gosarhro.order_documents.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.opencsv.CSVWriter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.gosarhro.order_documents.entity.Journal
import ru.gosarhro.order_documents.service.JournalService
import ru.gosarhro.order_documents.model.DocumentsFilter
import java.util.stream.Collectors
import jakarta.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/unloadJournal")
class JournalController (
    private val journalService: JournalService,
    private val objectMapper: ObjectMapper
) {
    @GetMapping
    fun unloadFromDb(model: Model): String {
        val digitDocsFromDb = journalService.getDigitDocsFromDb()
        model.addAttribute("documentsFilter", DocumentsFilter())
        model.addAttribute("digitDocsFromDb", digitDocsFromDb)
        return "unloadJournal"
    }

    @PostMapping
    fun unloadFromDbWithFilter(model: Model, @ModelAttribute("documentsFilter") documentsFilter: DocumentsFilter): String {
        val digitDocsFromDb = journalService.getDigitDocsFromDbWithFilter(documentsFilter!!)
        model.addAttribute("digitDocsFromDb", digitDocsFromDb)
        return "unloadJournal"
    }

    @PostMapping("/exportCSV")
    fun exportCsv(@ModelAttribute("documentsFilter") documentsFilter: DocumentsFilter, response: HttpServletResponse) {
        val filename = "journal.csv"
        response.contentType = "text/csv; charset=cp1251"
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
        val writer = CSVWriter(response.writer, ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)
        val head = arrayOf("№ п/п", "Фонд, опись, дело", "Исполнитель", "Дата оцифровывания", "Кол-во файлов", "Кол-во Мб")
        writer.writeNext(head)
        val digitDocsFromDb = journalService.getDigitDocsFromDb()
        val strings = digitDocsFromDb.stream().map { digitDocFromDb: Journal? -> objectMapper.convertValue(digitDocFromDb, Array<String>::class.java) }.collect(Collectors.toList())
        writer.writeAll(strings)
    }

    @GetMapping("/refresh")
    fun refresh(): String {
        journalService.refresh()
        return "redirect:/unloadJournal"
    }
}