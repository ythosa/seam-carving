package seamcarving.seams

import seamcarving.coverters.EnergyConverter
import java.awt.image.BufferedImage

data class Cost(val value: Double)
data class MatrixIndex(val i: Int, val j: Int)
class SeamGraph() {
    private var content: Array<Array<Pair<MatrixIndex, Cost>>> = arrayOf()

    constructor(matrix: Array<DoubleArray>) {
        for (i in matrix.indices) {
            var array = arrayOf<Pair<MatrixIndex, Cost>>()
            for (j in 1..matrix[i].lastIndex) {
                array += Pair(MatrixIndex(i, j), Cost(10.0))
            }
            content += array
        }
    }

    operator fun get(i: Int): Array<Pair<MatrixIndex, Cost>> {
        return content[i]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SeamGraph

        if (!content.contentDeepEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        return content.contentDeepHashCode()
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
