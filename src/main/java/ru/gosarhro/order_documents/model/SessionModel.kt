package ru.gosarhro.order_documents.model

import ru.gosarhro.order_documents.entity.Executor
import ru.gosarhro.order_documents.entity.Reader
import java.io.File

data class SessionModel(
    var reader: Reader,
    var executor: Executor,
    val theme: String,
    var documentModels: MutableList<DocumentModel> = ArrayList(),
    var documentFiles: MutableList<File> = ArrayList(),
)
