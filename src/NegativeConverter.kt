package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage

class NegativeConverter : ImageConverter {
    override fun createConvertedFrom(inName: String?, outName: String?) {
        if (inName != null && outName != null) {
            val imgWorker = ImageWorker(inName, outName)
            val image = imgWorker.createImage()
            this.convert(image)
            imgWorker.createImageFile(image)
        }
    }

    override fun convert(image: BufferedImage) {
        for (i in 0 until image.width) {
            for (j in 0 until image.height) {
                val color = Color(image.getRGB(i, j))
                val negative = Color(
                        255 - color.red,
                        255 - color.green,
                        255 - color.blue
                )
                image.setRGB(i, j, negative.rgb)
            }
        }
    }
}
