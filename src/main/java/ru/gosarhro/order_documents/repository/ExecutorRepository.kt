package ru.gosarhro.order_documents.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.gosarhro.order_documents.entity.Executor

interface ExecutorRepository : JpaRepository<Executor, Long>