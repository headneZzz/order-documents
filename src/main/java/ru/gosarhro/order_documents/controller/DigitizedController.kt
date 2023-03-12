package ru.gosarhro.order_documents.controller

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.gosarhro.order_documents.service.DigitizedService

@Controller
class DigitizedController(
    private val digitizedService: DigitizedService
) {

    @GetMapping("/digitized")
    fun getDigitized(model: Model, pageable: Pageable, @RequestParam(required = false) fod: String?): String {
        val fodsPage = digitizedService.getDigitized(fod, pageable)
        model.addAttribute("fodsPage", fodsPage)
        model.addAttribute("fod", fod)
        return "digitized"
    }
}
