package seamcarving.coverters

import java.awt.image.BufferedImage

interface ImageConverter {
    fun convert(image: BufferedImage)
    fun createConverted(inName: String?, outName: String?)
}
