package ru.gosarhro.order_documents.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.gosarhro.order_documents.entity.Folder

interface FolderRepository : JpaRepository<Folder, Long> {

}