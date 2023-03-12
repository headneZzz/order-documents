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

@Component
class AuthFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse

        if (req.requestURI == "/login" || isContent(req) || req.session.getAttribute("authentication") != null) {
            if (req.session.getAttribute("authentication") != null) {
                SecurityContextHolder.getContext().authentication =
                    req.session.getAttribute("authentication") as Authentication
            }
            chain.doFilter(req, res)
        } else {
            res.sendRedirect("/login")
        }
    }

    private fun isContent(req: HttpServletRequest): Boolean {
        return req.requestURI.contains("webjars") || req.requestURI.contains("images")
    }
}
