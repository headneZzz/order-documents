package ru.gosarhro.order_documents.controller

import jakarta.servlet.http.HttpSession
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import ru.gosarhro.order_documents.service.DigitizedService
import ru.gosarhro.order_documents.util.SessionHolder

@Controller
class DigitizedController(
    private val digitizedService: DigitizedService
) {

    // TODO: Сделать поиск и проверить добавление
    @GetMapping("/digitized")
    fun getDigitized(model: Model, pageable: Pageable, session: HttpSession): String {
        if (!SessionHolder.sessions.containsKey(session.id)) {
            return "redirect:/login"
        }
        val fodsPage = digitizedService.getDigitized(pageable)
        model.addAttribute("fodsPage", fodsPage)
        return "digitized"
    }
}
