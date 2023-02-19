package ru.gosarhro.order_documents.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Entity
@Table(name = "orders")
class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "fond")
    var fond: String? = null

    @Column(name = "op")
    var op: String? = null

    @Column(name = "document")
    var document: String? = null

    @ManyToOne
    @JoinColumn(name = "reader")
    var reader: Reader? = null

    @ManyToOne
    @JoinColumn(name = "executor")
    var executor: Executor? = null

    @Column(name = "receipt_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var receiptDate: LocalDate? = null

    @Column(name = "is_deleted")
    var isDeleted: Boolean? = null

    @Column(name = "theme")
    var theme: String? = null
}
