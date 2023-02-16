package ru.gosarhro.order_documents.controller

import com.opencsv.CSVWriter
import com.opencsv.bean.ColumnPositionMappingStrategy
import com.opencsv.bean.StatefulBeanToCsvBuilder
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.gosarhro.order_documents.entity.Order
import ru.gosarhro.order_documents.model.DocumentsFilter
import ru.gosarhro.order_documents.service.OrderService
import ru.gosarhro.order_documents.util.SessionHolder
import java.time.LocalDate
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import ru.gosarhro.order_documents.controller.dto.OrderDto

@Controller
@RequestMapping("/unload")
class UnloadController(private val orderService: OrderService) {
    @GetMapping
    fun unloadFromDb(model: Model): String {
        val documentsFromDb = orderService.getAll()
        model.addAttribute("documentsFilter", DocumentsFilter())
        return unloadSetAttributes(model, documentsFromDb)
    }

    @PostMapping
    fun unloadFromDbWithFilter(model: Model, @ModelAttribute("documentsFilter") documentsFilter: DocumentsFilter, session: HttpSession): String {
        SessionHolder.filters[session.id] = documentsFilter
        val filter = SessionHolder.filters[session.id]
        if (filter!!.dateFrom.isEmpty()) {
            filter.dateFrom = "2019-11-01"
        }
        if (filter.dateTo.isEmpty()) {
            filter.dateTo = LocalDate.now().toString()
        }
        val documentsFromDb = orderService.getAllByFilter(filter)
        return unloadSetAttributes(model, documentsFromDb)
    }

    private fun unloadSetAttributes(model: Model, documentsFromDb: List<Order?>?): String {
        model.addAttribute("documents", documentsFromDb)
        model.addAttribute("documentsCount", "Всего дел: " + documentsFromDb!!.size)
        val documentsCount = documentsFromDb.stream().map { a: Order? -> a!!.fond + '_' + a.op + '_' + a.document }.distinct().count()
        model.addAttribute("uniqueDocumentsCount", "Уникальных дел: $documentsCount")
        return "unload"
    }

    @GetMapping("/exportCSV")
    fun exportCsv(response: HttpServletResponse, session: HttpSession) {
        val filename = "documents.csv"
        response.contentType = "text/csv; charset=cp1251"
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
        val mappingStrategy = ColumnPositionMappingStrategy<OrderDto?>()
        mappingStrategy.type = OrderDto::class.java
        val columns = arrayOf("fond", "op", "document", "reader", "executor", "receiptDate")
        mappingStrategy.setColumnMapping(*columns)
        val writer = StatefulBeanToCsvBuilder<OrderDto?>(response.writer)
                .withMappingStrategy(mappingStrategy)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(';')
                .withOrderedResults(false)
                .build()
        val head = OrderDto(
            id = 0L,
            fond = "Фонд",
            op = "Опись",
            document = "Дело",
            reader = "Читатель",
            executor = "Исполнитель",
            receiptDate = "Дата"
        )

        writer.write(head)
        val orders = orderService.getAllByFilter(SessionHolder.filters[session.id]!!)
        val orderDtos = orders.map { order ->
            OrderDto(
                id = order.id!!,
                fond = order.fond!!,
                op = order.op!!,
                document = order.document!!,
                reader = order.reader!!.fullName!!,
                executor = order.executor!!.name!!,
                receiptDate = order.receiptDate!!.toString()
            )
        }.toList()
        writer.write(orderDtos)
    }
}