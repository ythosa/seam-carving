package seamcarving.seams

import seamcarving.coverters.EnergyConverter
import java.awt.image.BufferedImage

data class Cost(val value: Double)
data class MatrixIndex(val i: Int, val j: Int)
class SeamGraph() {
    private lateinit var content: MutableMap<MatrixIndex, MutableMap<MatrixIndex, Cost>>

    constructor(matrix: Array<DoubleArray>) {
        for (i in matrix.indices) {
            for (j in 1 until matrix[i].lastIndex) {
                content[MatrixIndex(i, j)] = mutableMapOf(
                        MatrixIndex(i + 1, j) to Cost(matrix[i + 1][j]),
                        MatrixIndex(i + 1, j - 1) to Cost(matrix[i + 1][j - 1]),
                        MatrixIndex(i + 1, j + 1) to Cost(matrix[i + 1][j + 1])
                )
            }
        }
    }

    operator fun get(i: MatrixIndex): MutableMap<MatrixIndex, Cost>? {
        return content[i]
    }
}

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
