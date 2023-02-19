package ru.gosarhro.order_documents.unload

import org.springframework.data.jpa.repository.JpaRepository

interface FolderRepository : JpaRepository<Folder, Long>
