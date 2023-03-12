package ru.gosarhro.order_documents.controller

import jakarta.servlet.http.HttpSession
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import ru.gosarhro.order_documents.config.AppConfig
import ru.gosarhro.order_documents.model.ReaderDetails
import ru.gosarhro.order_documents.service.OrdersService
import java.util.*

@Controller
class OrdersController(
    private val ordersService: OrdersService,
    private val appConfig: AppConfig
) {

    @GetMapping("/orders")
    fun showAddDocumentPage(
        model: Model,
        session: HttpSession,
        @AuthenticationPrincipal readerDetails: ReaderDetails
    ): String {
        if (session.getAttribute("errorMessage") != null) {
            model.addAttribute(
                "errorMessage",
                session.getAttribute("errorMessage")
            )
            session.removeAttribute("errorMessage")
        }
        if (session.getAttribute("successMessage") != null) {
            model.addAttribute(
                "successMessage",
                session.getAttribute("successMessage")
            )
            session.removeAttribute("successMessage")
        }
        val reader = readerDetails.reader
        val orders = ordersService.getReadersOrders(reader)
        if (appConfig.maxOrderSize != 0 && orders.size >= appConfig.maxOrderSize) {
            model.addAttribute(
                "warningMessage",
                "У вас в списке 20 дел. Вы не можете добавлять новые дела, пока не удалите из списка старые."
            )
        }
        if (orders.isEmpty()) {
            model.addAttribute(
                "infoMessage",
                "У вас пока нет дел в списке. Вы можете посмотреть и добавить дела для рассмотрения через кнопку Добавить."
            )
        }
        model.addAttribute("reader", reader)
        model.addAttribute("orders", orders)

        return "orders"
    }
}
