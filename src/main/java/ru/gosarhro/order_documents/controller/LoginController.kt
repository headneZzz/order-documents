package ru.gosarhro.order_documents.controller

import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.gosarhro.order_documents.entity.Executor
import ru.gosarhro.order_documents.model.LoginForm
import ru.gosarhro.order_documents.model.SessionModel
import ru.gosarhro.order_documents.service.LoginService
import ru.gosarhro.order_documents.util.SessionHolder

@Controller
@RequestMapping("/", "index", "login")
class LoginController(private val loginService: LoginService) {

    @GetMapping
    fun index(model: Model): String {
        val loginForm = LoginForm()
        val executors = loginService.getExecutors()
        model.addAttribute("loginForm", loginForm)
        model.addAttribute("executors", executors)
        return "login"
    }

    @PostMapping
    fun newReader(model: Model, @ModelAttribute("loginForm") loginForm: LoginForm, session: HttpSession): String {
        val readerFullName = loginForm.readerFullName.trim { it <= ' ' }
        val executor = loginForm.executor
        if (readerFullName.isEmpty() || executor == Executor()) {
            model.addAttribute("errorMessage", "Все поля должны быть заполнены")
            return "login"
        }
        if (loginService.readerFolderIsNotExists(readerFullName)) {
            model.addAttribute("errorMessage", "Папка читателя не создана. Обратитесь к сотруднику для создания папки.")
            return "login"
        }
        val reader = loginService.getReader(readerFullName)
        SessionHolder.sessions[session.id] = SessionModel(reader, executor)
        return "redirect:/order"
    }
}