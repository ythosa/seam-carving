package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt


const val basePath = "./test/input/"

data class XY(val x: Int, val y: Int)

data class XYPriority(val xy: XY, val priority: Double)

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

fun rotateImage(image: BufferedImage): BufferedImage {
    val transformed = BufferedImage(image.height, image.width, BufferedImage.TYPE_INT_RGB)
    for (i in 0 until image.width) {
        for (j in 0 until image.height) {
            transformed.setRGB(j, i, image.getRGB(i, j))
        }
    }
    return transformed
}

fun getMinSeamY(path: List<XY>): Int {
    return (path.sumBy { e -> e.y }) / path.size
}

fun removeFromSeam(image: BufferedImage, path: List<XY>, pixelsToRemove: Int): BufferedImage {
    var sizeFragmentToRemove = getMinSeamY(path)
    println("$sizeFragmentToRemove $pixelsToRemove")
    if (sizeFragmentToRemove > pixelsToRemove) sizeFragmentToRemove = pixelsToRemove

    val modifiedImage = BufferedImage(image.width - sizeFragmentToRemove, image.height, BufferedImage.TYPE_INT_RGB)

    if (sizeFragmentToRemove > image.width / 2) sizeFragmentToRemove -= image.width / 2

    for (i in sizeFragmentToRemove until image.width) {
        for (j in 0 until image.height) {
            modifiedImage.setRGB(i - sizeFragmentToRemove, j, image.getRGB(i, j))
        }
    }

    return modifiedImage
}

fun main(args: Array<String>) {
    var image = readImage(basePath + args[1])
    val outputFileName = "./${args[3]}"

    var widthToRemove = args[5].toInt()
    var heightToRemove = args[7].toInt()
    val outputWidth = image.width - widthToRemove
    val outputHeight = image.height - heightToRemove

    var gradientImage: GradientImage
    var energies: Array<DoubleArray>
    var shortestSeam: List<XY>

    while (image.width != outputWidth) {
        widthToRemove = image.width - outputWidth
        gradientImage = GradientImage(image)
        energies = Array(image.height) { DoubleArray(image.width) }
        repeat(image.height) { height ->
            repeat(image.width) { width ->
                val energy = gradientImage.energy(width, height)
                energies[height][width] = energy
            }
        }
        shortestSeam = findShortestPath(energies)
        image = removeFromSeam(image, shortestSeam, widthToRemove)
    }

    writeImage(image, outputFileName)
}

private fun normalizeEnergies(energies: Array<DoubleArray>, maxEnergy: Double): Array<IntArray> {

    val normalized = Array(energies.size) { IntArray(energies.first().size) }

    repeat(energies.size) { height ->
        repeat(energies[height].size) { width ->
            val energy = energies[height][width]
            val intensity = (255.0 * energy / maxEnergy).toInt()
            normalized[height][width] = intensity
        }
    }

    return normalized
}

private fun findShortestPath(weights: Array<DoubleArray>): List<XY> {
    val maxX = weights.first().lastIndex
    val maxY = weights.lastIndex

    val distances = Array(maxY + 1) { y ->
        DoubleArray(maxX + 1) {
            if (y == 0) weights[0][it]
            else Double.MAX_VALUE
        }
    }

    val queueComparator = Comparator<XYPriority> { p0, p1 -> p0.priority.compareTo(p1.priority) }
    val queue = PriorityQueue(queueComparator)
    for (x in 0..maxX) {
        queue.offer(XYPriority(XY(x, 0), weights[0][x]))
    }

    val prev = HashMap<XY, XY>()

    while (queue.isNotEmpty()) {
        val lowestWeight = queue.remove()
        val currentDistance = distances[lowestWeight.xy.y][lowestWeight.xy.x]

        val neighbors = getNeighbours(lowestWeight.xy, maxX, maxY)
        for (neighbor in neighbors) {
            val newDistance = currentDistance + weights[neighbor.y][neighbor.x]
            if (newDistance < distances[neighbor.y][neighbor.x]) {
                distances[neighbor.y][neighbor.x] = newDistance
                prev[neighbor] = lowestWeight.xy
                queue.offer(XYPriority(neighbor, newDistance))
            }
        }
    }

    val targetX = distances.last().withIndex().minBy { x -> x.value }!!.index
    val target = XY(targetX, distances.lastIndex)

    return reconstructPath(target, prev)
}

private fun getNeighbours(xy: XY, maxX: Int, maxY: Int): List<XY> {
    if (xy.y == maxY) return emptyList()

    val arr = arrayListOf<XY>()

    val lowerLeft = XY(xy.x - 1, xy.y + 1)
    if (lowerLeft.x >= 0) arr.add(lowerLeft)

    val lowerMedium = XY(xy.x, xy.y + 1)
    arr.add(lowerMedium)

    val lowerRight = XY(xy.x + 1, xy.y + 1)
    if (lowerRight.x <= maxX) arr.add(lowerRight)

    return arr
}

private fun reconstructPath(target: XY, prev: HashMap<XY, XY>): List<XY> {
    val path = arrayListOf<XY>()
    var current: XY? = target
    do {
        path.add(current!!)
        current = prev[current]
    } while (current != null)

    return path.reversed()
}

private fun readImage(fileName: String): BufferedImage {
    return ImageIO.read(File(fileName))!!
}

private fun writeImage(image: BufferedImage, fileName: String) {
    ImageIO.write(image, "png", File(fileName))
}
