package ru.gosarhro.order_documents.controller.dto

data class OrderDto(
    var id: Long,
    var fond: String,
    var op: String,
    var document: String,
    var reader: String,
    var executor: String,
    var receiptDate: String
)
