package ru.gosarhro.order_documents.controller

import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import ru.gosarhro.order_documents.model.OrderForm
import ru.gosarhro.order_documents.service.OrderService
import ru.gosarhro.order_documents.util.SessionHolder
import java.util.*

@Controller
class OrderController(private val orderService: OrderService) {
    @GetMapping("/order")
    fun showAddDocumentPage(model: Model, session: HttpSession): String {
        if (!SessionHolder.sessions.containsKey(session.id)) {
            return "redirect:/login"
        }
        model.addAttribute("reader", SessionHolder.sessions[session.id]!!.reader)
        model.addAttribute("orderForm", OrderForm())

        return "order"
    }

    @PostMapping("/order")
    fun saveDocument(model: Model, @ModelAttribute("orderForm") orderForm: OrderForm, session: HttpSession): String {
        model.addAttribute("reader", SessionHolder.sessions[session.id]!!.reader)
        val orderedDocuments = orderForm.documents.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toList()
        val invalidDoc = orderService.getFirstInvalidDoc(orderedDocuments)
        if (invalidDoc != null) {
            model.addAttribute("errorMessage", "Неправильно расставлены пробелы в $invalidDoc")
            return "order"
        }
        val documentsNotFound = orderService.findDocs(orderedDocuments, session.id)
        if (documentsNotFound.isNotEmpty()) {
            model.addAttribute("errorMessage", "$documentsNotFound нет в базе.")
            return "order"
        }
        return "redirect:/load"
    }

    @GetMapping("/load")
    fun loadScreen(): String = "load"

    @GetMapping("/send")
    fun saveAndLoadDocumentsList(session: HttpSession): String {
        orderService.sendDoc(session.id)
        return "redirect:/login"
    }
}