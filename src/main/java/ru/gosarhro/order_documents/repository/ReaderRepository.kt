package ru.gosarhro.order_documents.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.gosarhro.order_documents.entity.Reader

interface ReaderRepository : JpaRepository<Reader, Long> {
    fun findByFullName(fullName: String): Reader?
}