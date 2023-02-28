package ru.gosarhro.order_documents.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import ru.gosarhro.order_documents.service.ImageService

@Controller
class ImageController(
    private val imageService: ImageService
) {

    @GetMapping("/images/{filename:.+}")
    fun getImage(@PathVariable filename: String): ResponseEntity<ByteArray> {
        val file = imageService.getImage(filename) ?: return ResponseEntity.badRequest().build()
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        headers.contentLength = file.size.toLong()
        return ResponseEntity.ok().headers(headers).body(file)
    }

    @GetMapping("/images/preview")
    fun getPreview(@RequestParam fod: String): ResponseEntity<ByteArray> {
        val file = imageService.getPreview(fod) ?: return ResponseEntity.badRequest().build()
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        headers.contentLength = file.size.toLong()
        return ResponseEntity.ok().headers(headers).body(file)
    }
}
