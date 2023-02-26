package ru.gosarhro.order_documents.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.gosarhro.order_documents.entity.Digitized

interface DigitizedRepository : JpaRepository<Digitized, Long> {

    @Query("select distinct d.fod from Digitized d where :fod is null or d.fod like %:fod%")
    fun findAllFods(fod: String?, pageable: Pageable): Page<String>

    fun findAllByFileNameStartsWith(fod: String): List<Digitized>

    @Query("select d from Digitized d where d.fileName like %:fileName%")
    fun findFirstByFileNameIsLike(fileName: String, pageable: Pageable): List<Digitized>
}
