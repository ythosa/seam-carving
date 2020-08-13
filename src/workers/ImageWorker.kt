package workers

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


class ImageWorker(private val inPath: String, private val outPath: String) {
    fun getImage(): BufferedImage {
        val file = File(inPath)
        return ImageIO.read(file)
    }

    fun createImageFile(image: BufferedImage) {
        val file = File(outPath)
        ImageIO.write(image, "png", file)
    }
}
