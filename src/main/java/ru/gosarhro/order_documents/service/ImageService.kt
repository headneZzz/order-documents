package ru.gosarhro.order_documents.service

import lombok.SneakyThrows
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
import kotlin.math.roundToInt

@Service
class ImageService(
    private val digitizedRepository: DigitizedRepository,
) {
    fun getImage(fileName: String): ByteArray? {
        val newFileName = fileName.replace(" ", "_")
        val digitization = digitizedRepository.findByFileName(newFileName) ?: return null
        val file = File(digitization.ref!!.trim { it <= '#' })
        val image = ImageIO.read(file)
        val imageWithoutAlpha = optimize(image)
        return compressImage(imageWithoutAlpha)
    }

    fun getPreview(fod: String): ByteArray? {
        val newFod = fod.replace(" ", "_")
        val digitization = digitizedRepository.findByFileName(newFod + "_000") ?: return null
        val file = File(digitization.ref!!.trim { it <= '#' })
        val image = ImageIO.read(file)
        val imageWithoutAlpha = optimize(image)
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

    /**
     * @return image reduced to the size specified by properties without alpha channel
     */
    @SneakyThrows
    private fun optimize(originalImage: BufferedImage): BufferedImage {
        val originalWidth = originalImage.width
        val originalHeight = originalImage.height
        if (originalWidth < MAX_WIDTH && originalHeight < MAX_HEIGHT && !originalImage.colorModel.hasAlpha()) {
            return originalImage
        }
        var newWidth = originalWidth
        var newHeight = originalHeight
        if (originalWidth > MAX_WIDTH || originalHeight > MAX_HEIGHT) {
            if (originalWidth > originalHeight) {
                newWidth = MAX_WIDTH
                newHeight = (originalHeight.toDouble() / originalWidth * newWidth).roundToInt()
            } else {
                newHeight = MAX_HEIGHT
                newWidth = (originalWidth.toDouble() / originalHeight * newHeight).roundToInt()
            }
        }
        val resizedImage = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
        val g: Graphics2D = resizedImage.createGraphics()
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null)
        g.dispose()

        return resizedImage
    }

    companion object {
        const val MAX_WIDTH = 1920
        const val MAX_HEIGHT = 1920
    }
}
