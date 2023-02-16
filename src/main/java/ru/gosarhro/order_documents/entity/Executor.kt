package ru.gosarhro.order_documents.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "digitization.Исполнители")
class Executor {

    @Id
    @Column(name = "Код_исполнителя")
    var code: Long? = null

    @Column(name = "Исполнитель")
    var name: String? = null
}
