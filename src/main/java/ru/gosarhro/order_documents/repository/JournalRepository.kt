package ru.gosarhro.order_documents.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.gosarhro.order_documents.entity.Journal

interface JournalRepository : JpaRepository<Journal, Long> {
    fun findAllByDigitizationDateBetweenAndExecutor_NameIsLikeIgnoreCase(
        dateFrom: String,
        dateTo: String,
        executor: String
    ): List<Journal>
}