package ru.gosarhro.order_documents.unload

import org.springframework.data.jpa.repository.JpaRepository

interface JournalRepository : JpaRepository<Journal, Long> {
    fun findAllByDigitizationDateBetweenAndExecutor_NameIsLikeIgnoreCase(
        dateFrom: String,
        dateTo: String,
        executor: String
    ): List<Journal>
}
