package imageModifiers.seamcarving

import imageModifiers.ImageModifier
import imageModifiers.converters.EnergyConverter
import workers.InputData
import java.awt.image.BufferedImage
import java.util.*


class SeamCarving(override val image: BufferedImage, override val data: InputData) : ImageModifier {
    override fun get(): BufferedImage {
        if (data.verticalSeamsToRemove == null || data.horizontalSeamsToRemove == null) {
            throw Error("verticalSeamsToRemove & horizontalSeamsToRemove must be initialized")
        }

        var processedImage = image
        repeat(data.verticalSeamsToRemove) {
            print("Removing vertical seam ${it + 1} of ${data.verticalSeamsToRemove}...")
            val energies = EnergyConverter(processedImage, data).getEnergyMatrix()
            val shortestVerticalSeam = findShortestPath(energies)
            processedImage = removeVerticalSeam(processedImage, shortestVerticalSeam)
            println("Done")
        }

        repeat(data.horizontalSeamsToRemove) {
            print("Removing horizontal seam ${it + 1} of ${data.horizontalSeamsToRemove}...")
            val energies = EnergyConverter(processedImage, data).getEnergyMatrix()
            val energiesTransposed = transpose(energies)
            val shortestHorizontalSeam = findShortestPath(energiesTransposed)
            processedImage = removeHorizontalSeam(processedImage, shortestHorizontalSeam)
            println("Done")
        }

        return processedImage
    }

    private fun findShortestPath(weights: Array<DoubleArray>): List<Int> {
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

    private fun removeVerticalSeam(sourceImage: BufferedImage, seam: List<Int>): BufferedImage {
        val targetImage = BufferedImage(sourceImage.width - 1, sourceImage.height, sourceImage.type)

        repeat(sourceImage.height) { height ->
            var newWidth = 0
            val seamWidth = seam[height]

            repeat(sourceImage.width) { width ->
                if (width != seamWidth) {
                    val rgb = sourceImage.getRGB(width, height)
                    targetImage.setRGB(newWidth, height, rgb)
                    newWidth++
                }
            }
        }

        return targetImage
    }

    private fun removeHorizontalSeam(sourceImage: BufferedImage, seam: List<Int>): BufferedImage {
        val targetImage = BufferedImage(sourceImage.width, sourceImage.height - 1, sourceImage.type)

        repeat(sourceImage.width) { width ->
            var newHeight = 0
            val seamHeight = seam[width]

            repeat(sourceImage.height) { height ->
                if (height != seamHeight) {
                    val rgb = sourceImage.getRGB(width, height)
                    targetImage.setRGB(width, newHeight, rgb)
                    newHeight++
                }
            }
        }

        return targetImage
    }

    private fun transpose(data: Array<DoubleArray>): Array<DoubleArray> {
        val transposed = Array(data.first().size) { DoubleArray(data.size) }

        repeat(data.size) { height ->
            repeat(data[height].size) { width ->
                transposed[width][height] = data[height][width]
            }
        }

        return transposed
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

    private fun reconstructPath(target: XY, prev: HashMap<XY, XY>): List<Int> {
        val path = arrayListOf<Int>()
        var current: XY? = target
        do {
            path.add(current!!.x)
            current = prev[current]
        } while (current != null)

        return path.reversed()
    }
}
