package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage;
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.pow
import kotlin.math.sqrt

fun main(args: Array<String>) {
    val inName = getArgValue(args, "-in")
    val outName = getArgValue(args, "-out")
    energyImageConverter(inName, outName)
//    toNegativeImageConverter(inName, outName)
}

private fun energyImageConverter(inName: String?, outName: String?) {
    if (inName != null && outName != null) {
        val image = createImage(inName)
        convertToEnergy(image)
        createImageFile(image, outName)
    }
}

fun convertToEnergy(image: BufferedImage) {
    val energyArrayOfImage = getEnergyArrayOfImage(image)
    val maxEnergyValue = getMaxEnergyValueOfImage(energyArrayOfImage)
    for (i in 0 until image.width) {
        for (j in 0 until image.height) {
            image.setRGB(i, j, getNormalizedEnergyPixel(energyArrayOfImage[i][j], maxEnergyValue).rgb)
        }
    }
}

fun getEnergyArrayOfImage(image: BufferedImage): Array<DoubleArray> {
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

fun getMaxEnergyValueOfImage(energyArrayOfImage: Array<DoubleArray>): Double {
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

fun normalizeEnergyOfPixel(energy: Double, maxEnergyValue: Double): Int {
    return (255.0 * energy / maxEnergyValue).toInt()
}

fun getNormalizedEnergyPixel(energy: Double, maxEnergyValue: Double): Color {
    val intensity = normalizeEnergyOfPixel(energy, maxEnergyValue)
    return Color(intensity, intensity, intensity)
}

private fun toNegativeImageConverter(inName: String?, outName: String?) {
    if (inName != null && outName != null) {
        val image = createImage(inName)
        convertToNegative(image)
        createImageFile(image, outName)
    }
}

private fun getArgValue(args: Array<String>, arg: String): String? {
    for (i in args.indices) {
        if (args[i] == arg && i + 1 < args.size) {
            return args[i + 1]
        }
    }

    return null
}

private fun createImage(path: String): BufferedImage {
    val file = File(path)
    return ImageIO.read(file)
}

private fun convertToNegative(image: BufferedImage) {
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

private fun createImageFile(image: BufferedImage, fileName: String) {
    val file = File(fileName)
    ImageIO.write(image, "png", file)
}

private fun drawCrossedOutRectangle() {
    val scanner = Scanner(System.`in`)

    val (width, height) = getRectangleSize(scanner)
    val outputPath = getImageName(scanner)

    val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val graphics = img.graphics

    graphics.color = Color.BLACK
    graphics.drawRect(0, 0, width - 1, height - 1)

    graphics.color = Color.RED
    graphics.drawLine(0, 0, width - 1, height - 1)
    graphics.drawLine(width - 1, 0, 0, height - 1)

    ImageIO.write(img, "png", File(outputPath))
}

private fun getRectangleSize(scanner: Scanner): Pair<Int, Int> {
    println("Enter rectangle width:")
    val w = scanner.nextInt()

    println("Enter rectangle height:")
    val h = scanner.nextInt()

    return Pair(w, h)
}

private fun getImageName(scanner: Scanner): String {
    println("Enter output image name:")
    return scanner.next()
}
