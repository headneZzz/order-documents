package ru.gosarhro.order_documents.service

import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.config.AppConfig
import ru.gosarhro.order_documents.entity.Executor
import ru.gosarhro.order_documents.entity.Reader
import ru.gosarhro.order_documents.repository.ExecutorRepository
import ru.gosarhro.order_documents.repository.ReaderRepository
import java.io.File

@Service
class LoginService(
    private val executorRepository: ExecutorRepository,
    private val readerRepository: ReaderRepository,
    private val appConfig: AppConfig
) {
    fun readerFolderIsNotExists(reader: String): Boolean {
        val readerFolder = File(appConfig.readingRoomPath + File.separator + reader)
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
