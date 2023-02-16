package ru.gosarhro.order_documents.entity

import jakarta.persistence.*

@Entity
@Table(name = "folders")
class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "path", nullable = false)
    var path: String? = null
}