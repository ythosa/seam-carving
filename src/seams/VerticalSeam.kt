package seamcarving.seams

import seamcarving.coverters.EnergyConverter
import java.awt.image.BufferedImage

class VerticalSeam {
    fun get(image: BufferedImage) {
        val energyMatrix = fitEnergyMatrix(EnergyConverter().getEnergyMatrixOfImage(image))

    }

    private fun fitEnergyMatrix(matrix: Array<DoubleArray>): Array<DoubleArray> {
        val fitted = Array(matrix.size + 2) { DoubleArray(matrix[0].size) }
        for (i in matrix[0].indices) {
            fitted[0][i] = 0.0
            fitted[matrix.size + 1][i] = 0.0
        }
        return fitted
    }

    fun getGraph(matrix: Array<DoubleArray>): Array<Array<Pair<MatrixIndex, Cost>>> {
        val graph = arrayOf<Array<Pair<MatrixIndex, Cost>>>()

        return graph
    }
}
