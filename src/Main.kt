package seamcarving

import seamcarving.workers.InputWorker
import java.awt.Color
import java.awt.image.BufferedImage;
import java.io.File
import java.util.*
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val inputWorker = InputWorker()
    val inName = inputWorker.getArgValue(args, "-in")
    val outName = inputWorker.getArgValue(args, "-out")
//    energyImageConverter(inName, outName)
//    toNegativeImageConverter(inName, outName)
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
