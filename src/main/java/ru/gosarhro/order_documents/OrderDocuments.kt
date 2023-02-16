package ru.gosarhro.order_documents

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class OrderDocuments

fun main(args: Array<String>) {
    runApplication<OrderDocuments>(*args)
}
