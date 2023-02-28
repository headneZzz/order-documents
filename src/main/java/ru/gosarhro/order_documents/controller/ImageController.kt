package ru.gosarhro.order_documents.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import ru.gosarhro.order_documents.service.ImageService
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_4BYTE_ABGR

@Controller
class ImageController(
    private val imageService: ImageService
) {

    @GetMapping("/images/{filename:.+}")
    fun getImage(@PathVariable filename: String): ResponseEntity<BufferedImage> {
        val file = imageService.getImage(filename) ?: return ResponseEntity.badRequest().build()
        var contentType = MediaType.IMAGE_JPEG
        if (file.type == TYPE_4BYTE_ABGR) {
            contentType = MediaType.IMAGE_PNG
        }
        return ResponseEntity.ok().contentType(contentType).body(file)
    }

    @GetMapping("/images/preview")
    fun getPreview(@RequestParam fod: String): ResponseEntity<BufferedImage> {
        val file = imageService.getPreview(fod) ?: return ResponseEntity.badRequest().build()
        var contentType = MediaType.IMAGE_JPEG
        if (file.type == TYPE_4BYTE_ABGR) {
            contentType = MediaType.IMAGE_PNG
        }
        return ResponseEntity.ok().contentType(contentType).body(file)
    }
}
