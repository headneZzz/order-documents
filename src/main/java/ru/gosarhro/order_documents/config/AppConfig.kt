package ru.gosarhro.order_documents.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.config")
data class AppConfig(
    val readerTimeout: Long = 120,
    val maxOrderSize: Int = 20,
    val readingRoomPath: String
)
