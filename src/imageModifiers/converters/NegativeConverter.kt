package imageModifiers.converters

import imageModifiers.ImageModifier
import java.awt.Color
import java.awt.image.BufferedImage

class NegativeConverter : ImageModifier {
    override fun get(image: BufferedImage) {
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
