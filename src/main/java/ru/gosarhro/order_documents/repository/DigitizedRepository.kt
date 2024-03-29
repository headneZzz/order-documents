package ru.gosarhro.order_documents.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.gosarhro.order_documents.entity.Digitized

interface DigitizedRepository : JpaRepository<Digitized, Long> {

    @Query("select distinct d.fod from Digitized d where :fod is null or d.fod = :fod")
    fun findAllFods(fod: String?, pageable: Pageable): Page<String>

    fun findAllByFileNameStartsWithOrderByFileName(fod: String): List<Digitized>

    fun findByFileName(fileName: String): Digitized?
}
