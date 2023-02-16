package ru.gosarhro.order_documents.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.entity.Executor
import ru.gosarhro.order_documents.entity.Reader
import ru.gosarhro.order_documents.repository.ExecutorRepository
import ru.gosarhro.order_documents.repository.ReaderRepository
import java.io.File

@Service
class LoginService(
    private val executorRepository: ExecutorRepository,
    private val readerRepository: ReaderRepository
) {

    @Value("\${app.config.reader-folders-path}")
    lateinit var readerFoldersPath: String

    fun readerFolderIsNotExists(reader: String): Boolean {
        val readerFolder = File(readerFoldersPath + File.separator + reader)
        return !readerFolder.exists()
    }

    fun getExecutors(): List<Executor> {
        return executorRepository.findAll()
    }

    fun getReader(fullName: String): Reader {
        var reader = readerRepository.findByFullName(fullName)
        if (reader == null) {
            reader = Reader()
            reader.fullName = fullName
            reader = readerRepository.save(reader)
        }
        return reader
    }
}