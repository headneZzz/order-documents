package ru.gosarhro.order_documents.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.gosarhro.order_documents.entity.Digitized

interface DigitizedRepository : JpaRepository<Digitized, Long> {

    @Query("SELECT DISTINCT d.fod FROM Digitized d")
    fun findAllFods(pageable: Pageable): Page<String>

    fun findAllByFileNameStartsWith(fod: String): List<Digitized>

    fun findFirstByFileName(fileName: String): Digitized
}
