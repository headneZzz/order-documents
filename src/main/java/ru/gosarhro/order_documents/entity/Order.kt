package ru.gosarhro.order_documents.entity

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import jakarta.persistence.*

@Entity
@Table(name = "orders")
class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id", unique = true, nullable = false, precision = 20, scale = 0)
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
}