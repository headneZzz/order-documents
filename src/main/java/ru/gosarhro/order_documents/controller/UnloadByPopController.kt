package ru.gosarhro.order_documents.controller

import com.opencsv.CSVWriter
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.gosarhro.order_documents.entity.Order
import ru.gosarhro.order_documents.model.DocumentsFilter
import ru.gosarhro.order_documents.service.OrdersService
import ru.gosarhro.order_documents.util.SessionHolder
import java.time.LocalDate
import java.util.*

@Controller
@RequestMapping("/unloadByPop")
class UnloadByPopController(private val ordersService: OrdersService) {
    private var docsByPop: Map<String, Int>? = null

    @GetMapping
    fun unloadFromDb(model: Model): String {
        val documentsFromDb = ordersService.getAll()
        model.addAttribute("documentsFilter", DocumentsFilter())
        return unloadSetAttributes(model, documentsFromDb)
    }

    @PostMapping
    fun unloadFromDbWithFilter(
        model: Model,
        @ModelAttribute("documentsFilter") documentsFilter: DocumentsFilter,
        session: HttpSession
    ): String {
        SessionHolder.filters[session.id] = documentsFilter
        val filter = SessionHolder.filters[session.id]
        if (filter!!.dateFrom.isEmpty()) {
            filter.dateFrom = "2019-11-01"
        }
        if (filter.dateTo.isEmpty()) {
            filter.dateTo = LocalDate.now().toString()
        }
        filter.reader = ""
        filter.executor = ""
        val documentsFromDb = ordersService.getAllByFilter(filter)
        return unloadSetAttributes(model, documentsFromDb)
    }

    private fun unloadSetAttributes(model: Model, documentsFromDb: List<Order?>?): String {
        val tempMap: MutableMap<String, Int> = HashMap()
        for (document in documentsFromDb!!) {
            val doc = document!!.fond + ' ' + document.op + ' ' + document.document
            tempMap.merge(doc, 1) { a: Int?, b: Int? -> Integer.sum(a!!, b!!) }
        }

        //Sort by value
        val docsByPop = tempMap.toList().sortedByDescending { (_, value) -> value }.toMap()
        model.addAttribute("documentsByPop", docsByPop)
        model.addAttribute("documentsCount", "Всего дел: " + docsByPop.size)
        return "unloadByPop"
    }

    @GetMapping("/exportCSV")
    fun exportCsv(response: HttpServletResponse) {
        val filename = "documentsByPop.csv"
        response.contentType = "text/csv; charset=cp1251"
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
        val writer = CSVWriter(
            response.writer, ';',
            CSVWriter.NO_QUOTE_CHARACTER,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END
        )
        val head = arrayOf("Дело", "Кол-во заказов")
        writer.writeNext(head)
        val entryList: List<Map.Entry<*, *>> = ArrayList<Map.Entry<*, *>>(
            docsByPop!!.entries
        )
        val docs: MutableList<Array<String>> = ArrayList()
        for (entry in entryList) {
            docs.add(entry.toString().split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        }
        writer.writeAll(docs)
    }
}
