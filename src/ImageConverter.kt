package seamcarving

import java.awt.image.BufferedImage

interface ImageConverter {
    fun convert(image: BufferedImage)
    fun createConvertedFrom(inName: String?, outName: String?)
}
