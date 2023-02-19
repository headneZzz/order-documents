package ru.gosarhro.order_documents.unload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import ru.gosarhro.order_documents.entity.Executor
import java.time.LocalDate

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
