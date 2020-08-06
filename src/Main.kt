package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage;
import java.io.File
import java.util.*
import javax.imageio.ImageIO


fun main(args: Array<String>) {

    val inName = getArgValue(args, "-in")
    val outName = getArgValue(args, "-out")

    if (inName != null && outName != null) {
        val inPath = "/Users/metalcyborg/Downloads/images/$inName"
        val image = createImage(inPath)
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

//fun main() {
//    val scanner = Scanner(System.`in`)
//
//    val (width, height) = getRectangleSize(scanner)
//    val outputPath = getImageName(scanner)
//
//    val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
//    val graphics = img.graphics
//
//    graphics.color = Color.BLACK
//    graphics.drawRect(0, 0, width - 1, height - 1)
//
//    graphics.color = Color.RED
//    graphics.drawLine(0, 0, width - 1, height - 1)
//    graphics.drawLine(width - 1, 0, 0, height - 1)
//
//    ImageIO.write(img, "png", File(outputPath))
//}

fun getRectangleSize(scanner: Scanner): Pair<Int, Int> {
    println("Enter rectangle width:")
    val w = scanner.nextInt()

    println("Enter rectangle height:")
    val h = scanner.nextInt()

    return Pair(w, h)
}

fun getImageName(scanner: Scanner): String {
    println("Enter output image name:")
    return scanner.next()
}
