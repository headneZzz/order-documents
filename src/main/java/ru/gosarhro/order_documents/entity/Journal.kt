package ru.gosarhro.order_documents.entity

import java.time.LocalDate
import jakarta.persistence.*

@Entity
@Table(name = "digitization.Сводный_журнал")
class Journal {

    @Id
    @Column(name = "Номер")
    var number: Long? = null

    @Column(name = "ФОД")
    var fod: String? = null

    @OneToOne
    @JoinColumn(name = "Исполнитель")
    var executor: Executor? = null

    @Column(name = "Дата_оцифровки")
    var digitizationDate: LocalDate? = null

    @Column(name = "Колво_файлов")
    var filesCount: Long? = null

    @Column(name = "Колво_мб")
    var sizeInMb: Double? = null
}