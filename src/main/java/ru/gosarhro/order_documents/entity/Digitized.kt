package ru.gosarhro.order_documents.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Formula
import java.time.LocalDate

@Entity
@Table(name = "Т_фотоЛисты")
class Digitized {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Код_файла", nullable = false)
    var id: Long? = null

    @Column(name = "Имя_файла", nullable = false)
    var fileName: String? = null

    @Column(name = "Ссылка", nullable = false)
    var ref: String? = null

    @ManyToOne
    @JoinColumn(name = "Исполнитель")
    val executor: Executor? = null

    @Column(name = "Дата_ввода", nullable = false)
    var createdDate: LocalDate? = null

    @Formula(value = "CONCAT(split_part(Имя_файла, '_', 1), ' ', split_part(Имя_файла, '_', 2), ' ', split_part(Имя_файла, '_', 3))")
    var fod: String? = null
}
