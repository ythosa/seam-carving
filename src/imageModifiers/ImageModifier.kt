package imageModifiers

import workers.InputData
import java.awt.image.BufferedImage


interface ImageModifier {
    val image: BufferedImage
    val data: InputData
    fun get(): BufferedImage
}
