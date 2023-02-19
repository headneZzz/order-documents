package ru.gosarhro.order_documents.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.repository.DigitizedRepository

@Service
class DigitizedService(
    private val digitizedRepository: DigitizedRepository
) {
    fun getDigitized(pageable: Pageable): Page<String> {
        return digitizedRepository.findAllFods(pageable)
    }
}
