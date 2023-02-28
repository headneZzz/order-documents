package ru.gosarhro.order_documents.service

import org.apache.poi.util.IOUtils
import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.repository.DigitizedRepository
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam

@Service
class ImageService(
    private val digitizedRepository: DigitizedRepository,
) {
    fun getImage(fileName: String): ByteArray? {
        val newFileName = fileName.replace(" ", "_")
        val digitization = digitizedRepository.findByFileName(newFileName) ?: return null
        val file = File(digitization.ref!!.trim { it <= '#' })
        val image = ImageIO.read(file)
        val imageWithoutAlpha = removeAlphaChannel(image)
        return compressImage(imageWithoutAlpha)
    }

    fun getPreview(fod: String): ByteArray? {
        val newFod = fod.replace(" ", "_")
        val digitization = digitizedRepository.findByFileName(newFod + "_000") ?: return null
        val file = File(digitization.ref!!.trim { it <= '#' })
        val image = ImageIO.read(file)
        val imageWithoutAlpha = removeAlphaChannel(image)
        return compressImage(imageWithoutAlpha)
    }

    private fun compressImage(image: BufferedImage): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val imageWriter = ImageIO.getImageWritersByFormatName("jpg").next()
        val imageOutputStream = ImageIO.createImageOutputStream(outputStream)
        imageWriter.output = imageOutputStream
        val param = imageWriter.defaultWriteParam
        param.compressionMode = ImageWriteParam.MODE_EXPLICIT
        param.compressionQuality = 0.7f
        imageWriter.write(null, IIOImage(image, null, null), param)
        imageOutputStream.flush()
        IOUtils.closeQuietly(outputStream)
        IOUtils.closeQuietly(imageOutputStream)
        return outputStream.toByteArray()
    }

    private fun removeAlphaChannel(image: BufferedImage): BufferedImage {
        if (!image.colorModel.hasAlpha()) {
            return image
        }
        val target = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
        val g: Graphics2D = target.createGraphics()
        g.fillRect(0, 0, image.width, image.height)
        g.drawImage(image, 0, 0, null)
        g.dispose()
        return target
    }
}
