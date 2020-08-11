package seamcarving.seams

import seamcarving.coverters.EnergyConverter
import java.awt.image.BufferedImage

class VerticalSeam {
    fun get(image: BufferedImage) {
        val energyMatrix = fitEnergyMatrix(EnergyConverter().getEnergyMatrixOfImage(image))

        val graph = SeamGraph(energyMatrix)
        val costs = SeamCosts(graph)
        val parents = SeamParents(graph)
        val processed = mutableListOf<MatrixIndex>()

        var node = costs.getLowest(processed)
        while (node != null) {
            val cost = costs[node]
            val neighbors = graph[node]
            for (n in neighbors?.keys!!) {
                val newCost = cost!! + neighbors[n]
                if (costs[n]!! > newCost) {
                    costs[n] = newCost
                    parents[n] = node!!
                }
                processed += node!!
                node = costs.getLowest(processed)
            }
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
