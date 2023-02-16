package ru.gosarhro.order_documents.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.gosarhro.order_documents.entity.Order
import ru.gosarhro.order_documents.model.DocumentsFilter
import ru.gosarhro.order_documents.service.UnloadService
import ru.gosarhro.order_documents.util.SessionHolder
import java.time.LocalDate

@Controller
@RequestMapping("/unload")
class UnloadController(private val unloadService: UnloadService) {
    @GetMapping
    fun getUnload(model: Model, pageable: Pageable): String {
        val ordersPage = unloadService.getAllOrders(pageable)
        model.addAttribute("documentsFilter", DocumentsFilter())
        return unloadSetAttributes(model, ordersPage)
    }

    @PostMapping
    fun unloadFromDbWithFilter(
        model: Model,
        @ModelAttribute("documentsFilter") documentsFilter: DocumentsFilter,
        session: HttpSession,
        pageable: Pageable
    ): String {
        SessionHolder.filters[session.id] = documentsFilter
        val filter = SessionHolder.filters[session.id]
        if (filter!!.dateFrom.isEmpty()) {
            filter.dateFrom = "2019-11-01"
        }
        if (filter.dateTo.isEmpty()) {
            filter.dateTo = LocalDate.now().toString()
        }
        val documentsFromDb = unloadService.getAllOrdersByFilter(filter, pageable)
        return unloadSetAttributes(model, documentsFromDb)
    }

    private fun unloadSetAttributes(model: Model, ordersPage: Page<Order>): String {
        val documentsCount =
            ordersPage.stream().map { a: Order? -> a!!.fond + '_' + a.op + '_' + a.document }.distinct().count()
        model.addAttribute("ordersPage", ordersPage)
        model.addAttribute("documentsCount", "Всего дел: " + ordersPage.size)
        model.addAttribute("uniqueDocumentsCount", "Уникальных дел: $documentsCount")
        return "unload"
    }

    @GetMapping("/exportCSV")
    fun exportCsv(response: HttpServletResponse, session: HttpSession) {
        unloadService.exportCsv(response, session)
    }
}
