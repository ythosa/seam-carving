package seamcarving.seams

data class Cost(val value: Double) {
    operator fun compareTo(c: Cost): Int {
        return if (value < c.value) 0 else 1
    }
}

data class MatrixIndex(val i: Int, val j: Int)

class SeamGraph() {
    private lateinit var content: MutableMap<MatrixIndex, MutableMap<MatrixIndex, Cost>>

    val vertices: MutableSet<MatrixIndex>
        get() = content.keys

    constructor(matrix: Array<DoubleArray>) {
        for (i in 0 until matrix.lastIndex) {
            for (j in matrix[i].indices) {
                when (j) {
                    0 -> content[MatrixIndex(i, j)] = mutableMapOf(
                            MatrixIndex(i + 1, j) to Cost(matrix[i + 1][j]),
                            MatrixIndex(i + 1, j + 1) to Cost(matrix[i + 1][j + 1]))
                    matrix[i].lastIndex -> content[MatrixIndex(i, j)] = mutableMapOf(
                            MatrixIndex(i + 1, j) to Cost(matrix[i + 1][j]),
                            MatrixIndex(i + 1, j - 1) to Cost(matrix[i + 1][j - 1]))
                    else -> content[MatrixIndex(i, j)] = mutableMapOf(
                            MatrixIndex(i + 1, j) to Cost(matrix[i + 1][j]),
                            MatrixIndex(i + 1, j - 1) to Cost(matrix[i + 1][j - 1]),
                            MatrixIndex(i + 1, j + 1) to Cost(matrix[i + 1][j + 1])
                    )
                }
            }
        }
    }

    operator fun get(i: MatrixIndex): MutableMap<MatrixIndex, Cost>? {
        return content[i]
    }
}

class SeamCosts() {
    private lateinit var content: MutableMap<MatrixIndex, Cost>
    val min: MatrixIndex
        get() {
            var minValue = Cost(0.0)
            var minIndex: MatrixIndex = content.keys.first()
            for ((k, v) in content) {
                if (v < minValue) {
                    minValue = v
                    minIndex = k
                }
            }

            return minIndex
        }

    constructor(graph: SeamGraph) {
        for (i in graph.vertices) {
            content[i] = Cost(Double.POSITIVE_INFINITY)
        }
        content[MatrixIndex(0, 0)] = Cost(0.0)
        for ((k, v) in graph[MatrixIndex(0, 0)]!!) {
            content[k] = v
        }
    }
}
