package seamcarving.seams

import seamcarving.coverters.EnergyConverter
import seamcarving.workers.ImageWorker
import java.awt.Color
import java.awt.image.BufferedImage

class VerticalSeam {
    fun getModified(inPath: String, outPath: String) {
        val imgWorker = ImageWorker(inPath, outPath)
        val image = imgWorker.getImage()
        this.get(image)
        imgWorker.createImageFile(image)
    }

    fun get(image: BufferedImage) {
        val energyMatrix = fitEnergyMatrix(EnergyConverter().getEnergyMatrixOfImage(image))

        val graph = SeamGraph(energyMatrix)
        graph.find()
        val path = graph.getPath(MatrixIndex(0, 0),
                MatrixIndex(energyMatrix.lastIndex, energyMatrix.first().lastIndex))

        for (e in path) {
            image.setRGB(e.i, e.j, Color.RED.rgb)
        }
    }

    private fun fitEnergyMatrix(matrix: Array<DoubleArray>): Array<DoubleArray> {
        val fitted = Array(matrix.size + 2) { DoubleArray(matrix[0].size) }
        for (i in matrix[0].indices) {
            fitted[0][i] = 0.0
            fitted[matrix.size + 1][i] = 0.0
        }
        return fitted
    }
}
