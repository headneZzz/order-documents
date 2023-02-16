package ru.gosarhro.order_documents.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.config.AppConfig
import ru.gosarhro.order_documents.entity.Digitized
import ru.gosarhro.order_documents.entity.Order
import ru.gosarhro.order_documents.entity.Reader
import ru.gosarhro.order_documents.model.DocumentModel
import ru.gosarhro.order_documents.model.DocumentsFilter
import ru.gosarhro.order_documents.repository.DigitizedRepository
import ru.gosarhro.order_documents.repository.FolderRepository
import ru.gosarhro.order_documents.repository.OrderRepository
import ru.gosarhro.order_documents.util.SessionHolder
import java.io.File
import java.io.File.separator
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.LocalDate
import java.util.*

@Service
class OrdersService(
    private val orderRepository: OrderRepository,
    private val folderRepository: FolderRepository,
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
        return digitizedRepository.findAllByFileNameStartsWith(fod)
    }

    fun getImage(fileName: String): Resource {
        val digitization = digitizedRepository.findFirstByFileName(fileName)
        val file = File(digitization.ref!!)
        return UrlResource(file.toURI())
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

    // TODO
    fun newOrder(digitizedId: Long, sessionId: String) {
        val digitized = digitizedRepository.findById(digitizedId)
        val order = Order()
        with(order) {
            fond = "123"
            op = "123"
            document = "123"
            reader = SessionHolder.sessions[sessionId]!!.reader
            executor = SessionHolder.sessions[sessionId]!!.executor
            receiptDate = LocalDate.now()
            isDeleted = false
        }
        orderRepository.save(order)
    }

    fun getFirstInvalidDoc(documents: List<String>): String? {
        for (doc in documents) {
            val document = doc.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (document.size != 3) {
                return doc
            }
        }
        return null
    }

    fun findDocs(documents: List<String>, sessionId: String): List<String> {
        val documentsNotFound: MutableList<String> = ArrayList()
        val folders: List<File> = folderRepository.findAll().map { File(it.path) }.toList()

        for (doc in documents) {
            val document = doc.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            //Ищем дело в главной папке
            val documentsDir =
                File(appConfig.mainDigitFolderPath + separator + document[0] + separator + document[1] + separator + document[2])
            if (documentsDir.exists()) {
                SessionHolder.sessions[sessionId]!!.documentModels.add(
                    DocumentModel(
                        document[0],
                        document[1],
                        document[2]
                    )
                )
                SessionHolder.sessions[sessionId]!!.documentFiles.addAll(documentsDir.listFiles())
                continue
            }

            //Ищем в именных папках
            val matchingFiles: MutableList<File> = ArrayList()
            var isFound = false
            for (folder in folders) {
                try {
                    searchFiles(folder, matchingFiles, document)
                } catch (e: NullPointerException) {
                    log.info(folder.name)
                    log.info("Files: " + Objects.requireNonNull(folder.listFiles()).size)
                    e.printStackTrace()
                }
                if (matchingFiles.size > 0) {
                    SessionHolder.sessions[sessionId]!!.documentModels.add(
                        DocumentModel(
                            document[0],
                            document[1],
                            document[2]
                        )
                    )
                    SessionHolder.sessions[sessionId]!!.documentFiles.addAll(matchingFiles)
                    isFound = true
                    break
                }
            }
            if (!isFound) {
                documentsNotFound.add(doc)
            }
        }
        return documentsNotFound
    }

    fun sendDocsToReadingRoom(sessionId: String) {
        for (doc in SessionHolder.sessions[sessionId]!!.documentFiles) {
            try {
                val cutName = doc.name.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val out = File(
                    appConfig.readingRoomPath + separator + SessionHolder.sessions[sessionId]!!.reader.fullName
                            + separator + cutName[0] + "_" + cutName[1] + "_" + cutName[2]
                )
                out.mkdirs()
                Files.copy(
                    doc.toPath(),
                    File(out.toString() + separator + doc.name).toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            } catch (e: Exception) {
                log.error("THIS FILE NAME: " + doc.name)
                log.error(SessionHolder.sessions[sessionId]!!.documentModels.toString())
                log.error("SIZE: " + SessionHolder.sessions[sessionId]!!.documentFiles.size)
                log.error(e.message)
                return
            }
        }

        saveOrder(sessionId)
        SessionHolder.clearSession(sessionId)
    }

    private fun saveOrder(sessionId: String) {
        for ((fond, op, doc) in SessionHolder.sessions[sessionId]!!.documentModels) {
            val order = Order()
            order.fond = fond
            order.op = op
            order.document = doc
            order.reader = SessionHolder.sessions[sessionId]!!.reader
            order.executor = SessionHolder.sessions[sessionId]!!.executor
            order.receiptDate = LocalDate.now()
            orderRepository.save(order)
        }
    }

    private fun searchFiles(directory: File, matchingFiles: MutableList<File>, document: Array<String>) {
        for (file in Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile) {
                if (file.name.startsWith(document[0] + "_" + document[1] + "_" + document[2] + "_")) {
                    matchingFiles.add(file)
                }
            } else if (file.isDirectory) {
                searchFiles(file, matchingFiles, document)
            }
        }
    }
}
