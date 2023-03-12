package ru.gosarhro.order_documents.model

import org.springframework.security.core.userdetails.User
import ru.gosarhro.order_documents.entity.Executor
import ru.gosarhro.order_documents.entity.Reader

class ReaderDetails(
    val reader: Reader,
    val executor: Executor,
    val theme: String
) : User(
    reader.fullName,
    "1",
    true,
    true,
    true,
    true,
    listOf()
)
