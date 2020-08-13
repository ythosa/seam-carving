package seamcarving.seams

data class Cost(val value: Double) {
    operator fun compareTo(c: Cost): Int {
        return if (value == c.value) 0 else if (value < c.value) -1 else 1
    }

    operator fun plus(cost: Cost?): Cost {
        return Cost(value + cost!!.value)
    }
}

data class MatrixIndex(val i: Int, val j: Int)

class SeamGraph(matrix: Array<DoubleArray>) {
    private var content: MutableMap<MatrixIndex, MutableMap<MatrixIndex, Cost>> = mutableMapOf()

    private var costs: SeamCosts
    private var parents: SeamParents
    private var processed: MutableList<MatrixIndex>

    val vertices: MutableSet<MatrixIndex>
        get() = content.keys

    init {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                when (i) {
                    matrix.lastIndex -> {
                        when (j) {
                            matrix[i].lastIndex -> content[MatrixIndex(i, j)] = mutableMapOf()
                            else -> content[MatrixIndex(i, j)] = mutableMapOf(
                                    MatrixIndex(i, j + 1) to Cost(matrix[i][j + 1]))
                        }
                    }
                    0 -> {
                        when (j) {
                            0 -> content[MatrixIndex(i, j)] = mutableMapOf(
                                    MatrixIndex(i, j + 1) to Cost(matrix[i][j + 1]),
                                    MatrixIndex(i + 1, j) to Cost(matrix[i + 1][j]),
                                    MatrixIndex(i + 1, j + 1) to Cost(matrix[i + 1][j + 1]))
                            matrix[i].lastIndex -> content[MatrixIndex(i, j)] = mutableMapOf(
                                    MatrixIndex(i + 1, j) to Cost(matrix[i + 1][j]),
                                    MatrixIndex(i + 1, j - 1) to Cost(matrix[i + 1][j - 1]))
                            else -> content[MatrixIndex(i, j)] = mutableMapOf(
                                    MatrixIndex(i, j + 1) to Cost(matrix[i][j + 1]),
                                    MatrixIndex(i + 1, j) to Cost(matrix[i + 1][j]),
                                    MatrixIndex(i + 1, j - 1) to Cost(matrix[i + 1][j - 1]),
                                    MatrixIndex(i + 1, j + 1) to Cost(matrix[i + 1][j + 1])
                            )
                        }
                    }
                    else -> when (j) {
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
        costs = SeamCosts(this) // TODO: DEPENDENCY INVERSION
        parents = SeamParents()
        processed = mutableListOf()
    }

    private fun find() {
        var node = costs.getLowest(processed)
        while (node != null) {
            val cost = costs[node]
            val neighbors = this[node]
            for (n in neighbors?.keys!!) {
                val newCost = cost!! + neighbors[n]
                if (costs[n]!! > newCost) {
                    costs[n] = newCost
                    parents[n] = node
                }
            }
            processed.plusAssign(node)
            node = costs.getLowest(processed)
        }
    }

    fun getPath(start: MatrixIndex, end: MatrixIndex): MutableList<MatrixIndex> {
        this.find()
        val result = mutableListOf<MatrixIndex>()

        var el = parents[end]
        while (el != start) {
            if (el?.i!! == 0) {
                result += el
                break
            }
            result += el
            el = parents[el]
        }

        result.reverse()

        return result
    }

    operator fun get(i: MatrixIndex): MutableMap<MatrixIndex, Cost>? {
        return content[i]
    }
}

class SeamCosts(graph: SeamGraph) {
    private var content: MutableMap<MatrixIndex, Cost> = mutableMapOf()

    init {
        for (i in graph.vertices) {
            content[i] = Cost(Double.POSITIVE_INFINITY)
        }
        content[MatrixIndex(0, 0)] = Cost(0.0)
        for ((k, v) in graph[MatrixIndex(0, 0)]!!) {
            content[k] = v
        }
    }

    fun getLowest(processed: MutableList<MatrixIndex>): MatrixIndex? {
        var minValue = Cost(Double.POSITIVE_INFINITY)
        var minIndex: MatrixIndex? = null
        for ((k, v) in content) {
            if (v < minValue && !processed.contains(k)) {
                minValue = v
                minIndex = k
            }
        }

        return minIndex
    }

    operator fun get(i: MatrixIndex): Cost? {
        return content[i]
    }

    operator fun set(n: MatrixIndex, value: Cost) {
        content[n] = value
    }
}

class SeamParents {
    private var content: MutableMap<MatrixIndex, MatrixIndex> = mutableMapOf()

    init {
        content[MatrixIndex(1, 0)] = MatrixIndex(0, 0)
        content[MatrixIndex(1, 1)] = MatrixIndex(0, 0)
    }

    operator fun set(n: MatrixIndex, value: MatrixIndex) {
        content[n] = value
    }

    operator fun get(el: MatrixIndex): MatrixIndex? {
        return content[el]
    }
}
