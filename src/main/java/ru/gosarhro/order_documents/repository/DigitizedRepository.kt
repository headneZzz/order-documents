package ru.gosarhro.order_documents.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.gosarhro.order_documents.entity.Digitized

interface DigitizedRepository : JpaRepository<Digitized, Long> {
    fun findAllByFileNameStartsWith(fod: String): List<Digitized>

    fun findFirstByFileName(fileName: String): Digitized
}
