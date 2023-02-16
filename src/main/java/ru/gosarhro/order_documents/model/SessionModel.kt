package ru.gosarhro.order_documents.model

import ru.gosarhro.order_documents.entity.Executor
import ru.gosarhro.order_documents.entity.Reader
import java.io.File
import java.util.ArrayList

data class SessionModel(
    var reader: Reader = Reader(),
    var executor: Executor = Executor(),
    var documentModels: MutableList<DocumentModel> = ArrayList(),
    var documentFiles: MutableList<File> = ArrayList(),
    private var documentsFilter: DocumentsFilter = DocumentsFilter()
)
