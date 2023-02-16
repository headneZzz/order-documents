package ru.gosarhro.order_documents.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.gosarhro.order_documents.entity.Order
import java.time.LocalDate

interface OrderRepository : JpaRepository<Order, Long> {

    @Query("SELECT c FROM Order c " +
            "WHERE c.receiptDate between :dateFrom and :dateTo " +
            "and (:reader is null or c.reader.fullName like :reader) " +
            "and (:executor is null" + " or c.executor.name = :executor)")
    fun findAll(dateFrom: LocalDate?, dateTo: LocalDate?, reader: String?, executor: String?): List<Order>
}
