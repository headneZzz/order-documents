package ru.gosarhro.order_documents.session

import ru.gosarhro.order_documents.model.SessionModel
import ru.gosarhro.order_documents.unload.DocumentsFilter

object SessionHolder {
    val sessions: MutableMap<String, SessionModel> = HashMap()
    val filters: MutableMap<String, DocumentsFilter> = HashMap()

    fun clearSession(id: String) {
        sessions[id]!!.documentModels.clear()
        sessions[id]!!.documentFiles.clear()
        sessions.remove(id)
    }
}
