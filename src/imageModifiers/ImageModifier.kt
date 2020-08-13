package imageModifiers

import java.awt.image.BufferedImage

interface ImageModifier {
    fun get(image: BufferedImage)
}
