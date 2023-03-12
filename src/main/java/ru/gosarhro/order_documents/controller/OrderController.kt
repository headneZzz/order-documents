package ru.gosarhro.order_documents.controller

import jakarta.servlet.http.HttpSession
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.gosarhro.order_documents.model.ReaderDetails
import ru.gosarhro.order_documents.service.OrdersService

@Controller
class OrderController(
    private val ordersService: OrdersService
) {

    @GetMapping("/order")
    fun getOrder(model: Model, @RequestParam id: Long): String {
        val order = ordersService.getById(id)
        val fod = "${order.fond}_${order.op}_${order.document}"
        val digitizedList = ordersService.getDocumentFiles(fod)
        model.addAttribute("digitizedList", digitizedList)
        model.addAttribute("documentFullNumber", fod.replace("_", " "))
        return "order"
    }

    @GetMapping("/order/delete")
    fun delete(@RequestParam id: Long): String {
        ordersService.deleteOrder(id)
        return "redirect:/orders"
    }

    @GetMapping("/order/new")
    fun newOrder(@RequestParam fod: String, @AuthenticationPrincipal readerDetails: ReaderDetails): String {
        ordersService.newOrder(fod, readerDetails)
        return "redirect:/orders"
    }

    @PostMapping("/order")
    fun send(
        model: Model,
        @RequestParam("digitizedIds", required = false) digitizedIds: List<Long>,
        session: HttpSession
    ): String {
        if (digitizedIds.size == 0) {
            model.addAttribute("errorMessage", "Не выбраны файлы")
            return "order"
        }
        ordersService.setFilesToSession(digitizedIds, session)
        return "redirect:/load"
    }

    @GetMapping("/load")
    fun loadScreen(): String = "load"

    @GetMapping("/send")
    fun saveAndLoadDocumentsList(session: HttpSession, @AuthenticationPrincipal readerDetails: ReaderDetails): String {
        val readerFullName = readerDetails.reader.fullName!!
        try {
            ordersService.sendDocsToReadingRoom(session, readerFullName)
        } catch (e: Exception) {
            session.setAttribute("errorMessage", "Ошибка при отправке файлов. Обратитесь к сотруднику.")
            return "redirect:/orders"
        }
        session.setAttribute("successMessage", "Файлы успешно отправлены. Обратитесь к сотруднику для их получения.")
        return "redirect:/orders"
    }
}
