package ru.gosarhro.order_documents.unload

data class DocumentsFilter(
    var dateFrom: String = "",
    var dateTo: String = "",
    var reader: String = "",
    var executor: String = ""
)
