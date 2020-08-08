package seamcarving.coverters

import java.awt.image.BufferedImage

interface ImageConverter {
    fun convert(image: BufferedImage)
    fun createConverted(inPath: String, outPath: String)
}
