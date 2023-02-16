package ru.gosarhro.order_documents.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.config")
data class AppConfig(
    val maxOrderSize: Int,
    val readingRoomPath: String,
    val mainDigitFolderPath: String,
    val readerFoldersPath: String
)
