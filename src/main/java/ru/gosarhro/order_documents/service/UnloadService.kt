package ru.gosarhro.order_documents.service

import com.opencsv.CSVWriter
import com.opencsv.bean.ColumnPositionMappingStrategy
import com.opencsv.bean.StatefulBeanToCsvBuilder
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.controller.dto.OrderDto
import ru.gosarhro.order_documents.entity.Order
import ru.gosarhro.order_documents.model.DocumentsFilter
import ru.gosarhro.order_documents.repository.OrderRepository
import ru.gosarhro.order_documents.util.SessionHolder
import java.time.LocalDate

@Service
class UnloadService(private val orderRepository: OrderRepository) {

    fun getAllOrders(pageable: Pageable): Page<Order> {
        return orderRepository.findAll(pageable)
    }

    fun getAllOrdersByFilter(df: DocumentsFilter): List<Order> {
        val reader = if (df.reader == "") null else df.reader
        val executor = if (df.executor == "") null else df.executor
        return orderRepository.findAll(
            LocalDate.parse(df.dateFrom),
            LocalDate.parse(df.dateTo),
            reader,
            executor
        )
    }

    fun getAllOrdersByFilter(df: DocumentsFilter, pageable: Pageable): Page<Order> {
        val reader = if (df.reader == "") null else df.reader
        val executor = if (df.executor == "") null else df.executor
        return orderRepository.findAll(
            LocalDate.parse(df.dateFrom),
            LocalDate.parse(df.dateTo),
            reader,
            executor,
            pageable
        )
    }

    fun exportCsv(response: HttpServletResponse, session: HttpSession) {
        val filename = "documents.csv"
        response.contentType = "text/csv; charset=cp1251"
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
        val mappingStrategy = ColumnPositionMappingStrategy<OrderDto?>()
        mappingStrategy.type = OrderDto::class.java
        val columns = arrayOf("fond", "op", "document", "reader", "executor", "receiptDate")
        mappingStrategy.setColumnMapping(*columns)
        val writer = StatefulBeanToCsvBuilder<OrderDto?>(response.writer)
            .withMappingStrategy(mappingStrategy)
            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
            .withSeparator(';')
            .withOrderedResults(false)
            .build()
        val head = OrderDto(
            id = 0L,
            fond = "Фонд",
            op = "Опись",
            document = "Дело",
            reader = "Читатель",
            executor = "Исполнитель",
            receiptDate = "Дата"
        )

        writer.write(head)
        val orders = getAllOrdersByFilter(SessionHolder.filters[session.id]!!)
        val orderDtos = orders.map { order ->
            OrderDto(
                id = order.id!!,
                fond = order.fond!!,
                op = order.op!!,
                document = order.document!!,
                reader = order.reader!!.fullName!!,
                executor = order.executor!!.name!!,
                receiptDate = order.receiptDate!!.toString()
            )
        }.toList()
        writer.write(orderDtos)
    }
}
