package seamcarving.coverters

import seamcarving.workers.ImageWorker
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt

class EnergyConverter : ImageConverter {
    override fun createConverted(inPath: String, outPath: String) {
        val imgWorker = ImageWorker(inPath, outPath)
        val image = imgWorker.createImage()
        this.convert(image)
        imgWorker.createImageFile(image)
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

    private fun getEnergyArrayOfImage(image: BufferedImage): Array<DoubleArray> {
        val energyArrayOfImage = Array(image.width) { DoubleArray(image.height) }
        for (i in 0 until image.width) {
            for (j in 0 until image.height) {
                val posX = when (i) {
                    0 -> 1
                    image.width - 1 -> image.width - 2
                    else -> i
                }
                val posY = when (j) {
                    0 -> 1
                    image.height - 1 -> image.height - 2
                    else -> j
                }

                val colorLeftX = Color(image.getRGB(posX - 1, j))
                val colorRightX = Color(image.getRGB(posX + 1, j))
                val gradientX = (colorLeftX.red.toDouble() - colorRightX.red).pow(2.0) +
                        (colorLeftX.green.toDouble() - colorRightX.green).pow(2.0) +
                        (colorLeftX.blue.toDouble() - colorRightX.blue).pow(2.0)

                val colorTopY = Color(image.getRGB(i, posY - 1))
                val colorDownY = Color(image.getRGB(i, posY + 1))
                val gradientY = (colorTopY.red.toDouble() - colorDownY.red).pow(2.0) +
                        (colorTopY.green.toDouble() - colorDownY.green).pow(2.0) +
                        (colorTopY.blue.toDouble() - colorDownY.blue).pow(2.0)

                energyArrayOfImage[i][j] = sqrt(gradientX + gradientY)
            }
        }
        return energyArrayOfImage
    }

    private fun getMaxEnergyValueOfImage(energyArrayOfImage: Array<DoubleArray>): Double {
        var maxValue = 0.0
        for (i in energyArrayOfImage.indices) {
            for (j in energyArrayOfImage[i].indices) {
                if (energyArrayOfImage[i][j] > maxValue) {
                    maxValue = energyArrayOfImage[i][j]
                }
            }
        }
        return maxValue
    }

    private fun normalizeEnergyOfPixel(energy: Double, maxEnergyValue: Double): Int {
        return (255.0 * energy / maxEnergyValue).toInt()
    }

    private fun getNormalizedEnergyPixel(energy: Double, maxEnergyValue: Double): Color {
        val intensity = normalizeEnergyOfPixel(energy, maxEnergyValue)
        return Color(intensity, intensity, intensity)
    }
}
