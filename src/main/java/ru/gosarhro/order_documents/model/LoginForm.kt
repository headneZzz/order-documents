package ru.gosarhro.order_documents.model

import ru.gosarhro.order_documents.entity.Executor

data class LoginForm (
    val readerFullName: String = "",
    val executor: Executor = Executor(),
    val theme: String = ""
)