package ru.gosarhro.order_documents.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.gosarhro.order_documents.entity.Order
import ru.gosarhro.order_documents.entity.Reader
import java.time.LocalDate

interface OrderRepository : JpaRepository<Order, Long> {

    @Query(
        "SELECT c FROM Order c " +
                "WHERE c.receiptDate between :dateFrom and :dateTo " +
                "and (:reader is null or c.reader.fullName like :reader) " +
                "and (:executor is null" + " or c.executor.name = :executor)"
    )
    fun findAll(
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        reader: String?,
        executor: String?,
        pageable: Pageable
    ): Page<Order>

    @Query(
        "SELECT c FROM Order c " +
                "WHERE c.receiptDate between :dateFrom and :dateTo " +
                "and (:reader is null or c.reader.fullName like :reader) " +
                "and (:executor is null" + " or c.executor.name = :executor)"
    )
    fun findAll(dateFrom: LocalDate?, dateTo: LocalDate?, reader: String?, executor: String?): List<Order>

    fun findAllByReaderAndIsDeletedIsFalse(reader: Reader): List<Order>
}
