package ru.gosarhro.order_documents.controller

import jakarta.servlet.http.HttpSession
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.gosarhro.order_documents.model.OrderForm
import ru.gosarhro.order_documents.service.OrdersService
import ru.gosarhro.order_documents.util.SessionHolder

@Controller
class OrderController(
    private val ordersService: OrdersService
) {

    @GetMapping("/order")
    fun getOrder(model: Model, @RequestParam id: Long, session: HttpSession): String {
        if (!SessionHolder.sessions.containsKey(session.id)) {
            return "redirect:/login"
        }
        val order = ordersService.getById(id)
        val fod = "${order.fond}_${order.op}_${order.document}"
        val digitizedList = ordersService.getDocumentFiles(fod)
        model.addAttribute("digitizedList", digitizedList)
        model.addAttribute("documentFullNumber", fod.replace("_", " "))
        return "order"
    }

    @GetMapping("/order/delete")
    fun delete(@RequestParam id: Long, session: HttpSession): String {
        if (!SessionHolder.sessions.containsKey(session.id)) {
            return "redirect:/login"
        }
        ordersService.deleteOrder(id)
        return "redirect:/orders"
    }

    @GetMapping("/order/new")
    fun newOrder(@RequestParam id: Long, session: HttpSession): String {
        if (!SessionHolder.sessions.containsKey(session.id)) {
            return "redirect:/login"
        }
        ordersService.newOrder(id, session.id)
        return "redirect:/orders"
    }

    //TODO: старый метод, все переделать
    @PostMapping("/order")
    fun saveDocument(model: Model, @ModelAttribute("orderForm") orderForm: OrderForm, session: HttpSession): String {
        model.addAttribute("reader", SessionHolder.sessions[session.id]!!.reader)
        val orderedDocuments = orderForm.documents.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toList()
        val invalidDoc = ordersService.getFirstInvalidDoc(orderedDocuments)
        if (invalidDoc != null) {
            model.addAttribute("errorMessage", "Неправильно расставлены пробелы в $invalidDoc")
            return "orders"
        }
        val documentsNotFound = ordersService.findDocs(orderedDocuments, session.id)
        if (documentsNotFound.isNotEmpty()) {
            model.addAttribute("errorMessage", "$documentsNotFound нет в базе.")
            return "orders"
        }
        return "redirect:/load"
    }

    @GetMapping("/load")
    fun loadScreen(): String = "load"

    @GetMapping("/send")
    fun saveAndLoadDocumentsList(session: HttpSession): String {
        ordersService.sendDocsToReadingRoom(session.id)
        return "redirect:/login"
    }

    @GetMapping("/order/images/{filename:.+}")
    fun getImage(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = ordersService.getImage(filename)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename + "\"").body(file)
    }
}
