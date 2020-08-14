package imageModifiers.seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.abs
import kotlin.math.sqrt


class GradientImage(private val image: BufferedImage) {
    fun energy(x: Int, y: Int): Double {
        val xNotBoundary = x.coerceIn(1, image.width - 2)
        val yNotBoundary = y.coerceIn(1, image.height - 2)

        val gradSum = xGradSquare(xNotBoundary, y) + yGradSquare(x, yNotBoundary)
        return sqrt(gradSum.toDouble())
    }

    private fun xGradSquare(x: Int, y: Int): Int {
        val redXDiff = redXDiff(x, y)
        val greenXDiff = greenXDiff(x, y)
        val blueXDiff = blueXDiff(x, y)

        return redXDiff * redXDiff + greenXDiff * greenXDiff + blueXDiff * blueXDiff
    }

    private fun yGradSquare(x: Int, y: Int): Int {
        val redYDiff = redYDiff(x, y)
        val greenYDiff = greenYDiff(x, y)
        val blueYDiff = blueYDiff(x, y)

        return redYDiff * redYDiff + greenYDiff * greenYDiff + blueYDiff * blueYDiff
    }

    private fun redXDiff(x: Int, y: Int): Int {
        return xDiff(x, y, Color::getRed)
    }

    private fun greenXDiff(x: Int, y: Int): Int {
        return xDiff(x, y, Color::getGreen)
    }

    private fun blueXDiff(x: Int, y: Int): Int {
        return xDiff(x, y, Color::getBlue)
    }

    private fun redYDiff(x: Int, y: Int): Int {
        return yDiff(x, y, Color::getRed)
    }

    private fun greenYDiff(x: Int, y: Int): Int {
        return yDiff(x, y, Color::getGreen)
    }

    private fun blueYDiff(x: Int, y: Int): Int {
        return yDiff(x, y, Color::getBlue)
    }

    private fun xDiff(x: Int, y: Int, getter: (Color) -> Int): Int {
        return colorDifference(x + 1, y, x - 1, y, getter)
    }

    private fun yDiff(x: Int, y: Int, getter: (Color) -> Int): Int {
        return colorDifference(x, y + 1, x, y - 1, getter)
    }

    private fun colorDifference(x1: Int, y1: Int, x2: Int, y2: Int, getter: (Color) -> Int): Int {
        val rgb1 = image.getRGB(x1, y1)
        val rgb2 = image.getRGB(x2, y2)

        val color1 = getter(Color(rgb1))
        val color2 = getter(Color(rgb2))
        return abs(color1 - color2)
    }
}
