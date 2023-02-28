package ru.gosarhro.order_documents.service

import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.repository.DigitizedRepository
import java.awt.AlphaComposite
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

@Service
class ImageService(
    private val digitizedRepository: DigitizedRepository
) {
    fun getImage(fileName: String): BufferedImage? {
        val newFileName = fileName.replace(" ", "_")
        val digitization = digitizedRepository.findByFileName(newFileName) ?: return null
        val location = digitization.ref!!.trim { it <= '#' }
        return readImage(location)
    }

    fun getPreview(fod: String): BufferedImage? {
        val newFod = fod.replace(" ", "_")
        val digitization = digitizedRepository.findByFileName(newFod + "_000") ?: return null
        val location = digitization.ref!!.trim { it <= '#' }
        return readImage(location)
    }

    fun readImage(location: String): BufferedImage {
        val file = File(location)
        val image = ImageIO.read(file)
        val type = image.type

        val resizedImage = BufferedImage(image.width, image.height, type)
        val g = resizedImage.createGraphics()

        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_ARGB_PRE) {
            g.composite = AlphaComposite.Src
        }

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g.drawImage(image, 0, 0, image.width, image.height, null)
        g.dispose()

        return resizedImage
    }
}
