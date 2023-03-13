package ru.gosarhro.order_documents.service

import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.config.AppConfig
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class TimeoutService(
    private val appConfig: AppConfig
) {

    fun createFile(readerFullName: String) {
        val timeoutFile = getTimeoutFile(readerFullName)
        if (!timeoutFile.exists()) {
            timeoutFile.createNewFile()
        }
        val ldt = getLastModified(readerFullName)
        val ld = ldt.toLocalDate()
        if (LocalDate.now().isAfter(ld)) {
            updateLastModified(timeoutFile)
        }
    }


    fun isTimeout(readerFullName: String): Boolean {
        val ldt = getLastModified(readerFullName)
        val now = LocalDateTime.now()
        if (now.minusMinutes(appConfig.readerTimeout).isAfter(ldt)) {
            return true
        }
        return false
    }

    fun isLessThan15MinutesToTimeout(readerFullName: String): Boolean {
        val ldt = getLastModified(readerFullName)
        val now = LocalDateTime.now()
        if (now.minusMinutes(appConfig.readerTimeout).plusMinutes(15).isAfter(ldt)) {
            return true
        }
        return false
    }


    private fun updateLastModified(timeoutFile: File) {
        val instant = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
        timeoutFile.setLastModified(instant.toEpochMilli())
        timeoutFile.createNewFile()
    }

    private fun getLastModified(readerFullName: String): LocalDateTime {
        val timeoutFile = getTimeoutFile(readerFullName)
        if (!timeoutFile.exists()) {
            timeoutFile.createNewFile()
        }
        val epoch = timeoutFile.lastModified()
        return Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    private fun getTimeoutFile(readerFullName: String): File {
        return File(
            appConfig.readingRoomPath + File.separator + readerFullName
                    + File.separator + "timeoutFile"
        )
    }

}
