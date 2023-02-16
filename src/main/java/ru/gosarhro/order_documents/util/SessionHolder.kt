package ru.gosarhro.order_documents.util

import ru.gosarhro.order_documents.model.DocumentsFilter
import ru.gosarhro.order_documents.model.SessionModel

object SessionHolder {
    val sessions: MutableMap<String, SessionModel> = HashMap()
    val filters: MutableMap<String, DocumentsFilter> = HashMap()

    fun clearSession(id: String) {
        sessions[id]!!.documentModels.clear()
        sessions[id]!!.documentFiles.clear()
        sessions.remove(id)
    }
}