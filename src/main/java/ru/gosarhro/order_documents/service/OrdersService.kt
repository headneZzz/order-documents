package ru.gosarhro.order_documents.service

import jakarta.servlet.http.HttpSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.config.AppConfig
import ru.gosarhro.order_documents.entity.Digitized
import ru.gosarhro.order_documents.entity.Order
import ru.gosarhro.order_documents.entity.Reader
import ru.gosarhro.order_documents.model.ReaderDetails
import ru.gosarhro.order_documents.repository.DigitizedRepository
import ru.gosarhro.order_documents.repository.OrderRepository
import ru.gosarhro.order_documents.unload.DocumentsFilter
import java.io.File
import java.io.File.separator
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.LocalDate

@Service
class OrdersService(
    private val orderRepository: OrderRepository,
    private val digitizedRepository: DigitizedRepository,
    private val appConfig: AppConfig
) {
    private val log: Logger = LoggerFactory.getLogger(OrdersService::class.java)

    fun getById(id: Long): Order {
        return orderRepository.findByIdOrNull(id)!!
    }

    fun getAll(): List<Order> {
        return orderRepository.findAll()
    }

    fun getDocumentFiles(fod: String): List<Digitized> {
        return digitizedRepository.findAllByFileNameStartsWithOrderByFileName(fod + "_")
    }

    fun getReadersOrders(reader: Reader): List<Order> {
        return orderRepository.findAllByReaderAndIsDeletedIsFalse(reader)
    }

    fun getAllByFilter(df: DocumentsFilter): List<Order> {
        val reader = if (df.reader == "") null else df.reader
        val executor = if (df.executor == "") null else df.executor
        return orderRepository.findAll(
            LocalDate.parse(df.dateFrom),
            LocalDate.parse(df.dateTo),
            reader,
            executor
        )
    }

    fun deleteOrder(id: Long) {
        val order = orderRepository.findByIdOrNull(id)
        order!!.isDeleted = true
        orderRepository.save(order)
    }

    fun setFilesToSession(digitizedIds: List<Long>, session: HttpSession) {
        val digitizedList = digitizedRepository.findAllById(digitizedIds)
        val files = digitizedList.map { File(it.ref!!.trim { it <= '#' }) }.toList()
        session.setAttribute("documentFiles", files)
    }

    fun newOrder(fod: String, readerDetails: ReaderDetails) {
        val fodSplited = fod.split(" ")
        val order = Order()
        with(order) {
            fond = fodSplited[0]
            op = fodSplited[1]
            document = fodSplited[2]
            reader = readerDetails.reader
            executor = readerDetails.executor
            receiptDate = LocalDate.now()
            isDeleted = false
            theme = readerDetails.theme
        }
        orderRepository.save(order)
    }

    fun sendDocsToReadingRoom(session: HttpSession, readerFullName: String) {
        val documentFiles = session.getAttribute("documentFiles") as List<File>
        for (file in documentFiles) {
            try {
                val cutName = file.name.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val out = File(
                    appConfig.readingRoomPath + separator + readerFullName
                            + separator + cutName[0] + "_" + cutName[1] + "_" + cutName[2]
                )
                out.mkdirs()
                Files.copy(
                    file.toPath(),
                    File(out.toString() + separator + file.name).toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            } catch (e: Exception) {
                log.error("Error with filename: $file.name")
                log.error(e.message)
                throw Exception(e.message)
            }
        }
        session.removeAttribute("documentFiles")
    }
}
