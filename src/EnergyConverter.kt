package seamcarving

import java.awt.image.BufferedImage

class EnergyConverter : ImageConverter {
    override fun createConvertedFrom(inName: String?, outName: String?) {
        if (inName != null && outName != null) {
            val imgWorker = ImageWorker(inName, outName)
            val image = imgWorker.createImage()
            this.convert(image)
            imgWorker.createImageFile(image)
        }
    }

    override fun convert(image: BufferedImage) {
        val energyArrayOfImage = getEnergyArrayOfImage(image)
        val maxEnergyValue = getMaxEnergyValueOfImage(energyArrayOfImage)
        for (i in 0 until image.width) {
            for (j in 0 until image.height) {
                image.setRGB(i, j, getNormalizedEnergyPixel(energyArrayOfImage[i][j], maxEnergyValue).rgb)
            }
        }
    }
}