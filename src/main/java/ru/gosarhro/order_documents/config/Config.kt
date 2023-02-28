package ru.gosarhro.order_documents.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.BufferedImageHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import java.awt.image.BufferedImage

@Configuration
class Config {

    @Bean
    fun bufferedImageHttpMessageConverter(): HttpMessageConverter<BufferedImage?>? {
        return BufferedImageHttpMessageConverter()
    }
}
