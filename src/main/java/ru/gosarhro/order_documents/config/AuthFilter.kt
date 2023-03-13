package ru.gosarhro.order_documents.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import ru.gosarhro.order_documents.model.ReaderDetails
import ru.gosarhro.order_documents.service.TimeoutService

@Component
class AuthFilter(private val timeoutService: TimeoutService) : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse

        if (req.requestURI != "/login" && !isContent(req) && req.session.getAttribute("authentication") == null) {
            res.sendRedirect("/login")
            return
        }

        if (req.session.getAttribute("authentication") != null) {
            val authentication = req.session.getAttribute("authentication") as Authentication
            val readerDetails = authentication.principal as ReaderDetails
            if (timeoutService.isTimeout(readerDetails.reader.fullName!!)) {
                req.session.setAttribute("timeout", true)
                req.session.removeAttribute("authentication")
                res.sendRedirect("/login")
                return
            }
            SecurityContextHolder.getContext().authentication = authentication
        }

        chain.doFilter(req, res)
    }

    private fun isContent(req: HttpServletRequest): Boolean {
        return req.requestURI.contains("webjars") || req.requestURI.contains("images")
    }
}
