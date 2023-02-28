package ru.gosarhro.order_documents.controller

import jakarta.servlet.http.HttpSession
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.gosarhro.order_documents.service.OrdersService
import ru.gosarhro.order_documents.session.SessionHolder

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
    fun newOrder(@RequestParam fod: String, session: HttpSession): String {
        if (!SessionHolder.sessions.containsKey(session.id)) {
            return "redirect:/login"
        }
        ordersService.newOrder(fod, session.id)
        return "redirect:/orders"
    }

    @PostMapping("/order")
    fun send(
        model: Model,
        @RequestParam("digitizedIds", required = false) digitizedIds: List<Long>,
        session: HttpSession
    ): String {
        if (!SessionHolder.sessions.containsKey(session.id)) {
            return "redirect:/login"
        }
        if (digitizedIds.size == 0) {
            model.addAttribute("errorMessage", "Не выбраны файлы")
            return "order"
        }
        ordersService.setFilesToSession(digitizedIds, session.id)
        return "redirect:/load"
    }

    @GetMapping("/load")
    fun loadScreen(): String = "load"

    @GetMapping("/send")
    fun saveAndLoadDocumentsList(session: HttpSession): String {
        try {
            ordersService.sendDocsToReadingRoom(session.id)
        } catch (e: Exception) {
            session.setAttribute("errorMessage", "Ошибка при отправке файлов. Обратитесь к сотруднику.")
            return "redirect:/orders"
        }
        session.setAttribute("successMessage", "Файлы успешно отправлены. Обратитесь к сотруднику для их получения.")
        return "redirect:/orders"
    }

    @GetMapping("/order/images/{filename:.+}")
    fun getImage(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = ordersService.getImage(filename) ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename + "\"").body(file)
    }

}
