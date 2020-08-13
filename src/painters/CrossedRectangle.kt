package seamcarving.painters

import seamcarving.workers.ImageWorker
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*

class CrossedRectangle(private val outPath: String) : ImagePainter {
    override fun draw() {
        val scanner = Scanner(System.`in`)
        val (width, height) = getRectangleSize(scanner)

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = image.graphics

        graphics.color = Color.BLACK
        graphics.drawRect(0, 0, width - 1, height - 1)

        graphics.color = Color.RED
        graphics.drawLine(0, 0, width - 1, height - 1)
        graphics.drawLine(width - 1, 0, 0, height - 1)

        ImageWorker("", outPath).createImageFile(image)
    }

    private fun getRectangleSize(scanner: Scanner): Pair<Int, Int> {
        println("Enter rectangle width:")
        val w = scanner.nextInt()

        println("Enter rectangle height:")
        val h = scanner.nextInt()

        return Pair(w, h)
    }
}
