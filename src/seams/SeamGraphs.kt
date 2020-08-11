package seamcarving.seams

data class Cost(val value: Double) {
    operator fun compareTo(c: Cost): Int {
        return if (value < c.value) 0 else 1
    }

    operator fun plus(cost: Cost?): Cost {
        return Cost(value + cost!!.value)
    }
}

data class MatrixIndex(val i: Int, val j: Int)

class SeamGraph() {
    private lateinit var content: MutableMap<MatrixIndex, MutableMap<MatrixIndex, Cost>>

    private lateinit var costs: SeamCosts
    private lateinit var parents: SeamParents
    private lateinit var processed: MutableList<MatrixIndex>

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

        costs = SeamCosts(this)
        parents = SeamParents(this)
        processed = mutableListOf<MatrixIndex>()
    }

    fun find() {
        var node = costs.getLowest(processed)
        while (node != null) {
            val cost = costs[node]
            val neighbors = this[node]
            for (n in neighbors?.keys!!) {
                val newCost = cost!! + neighbors[n]
                if (costs[n]!! > newCost) {
                    costs[n] = newCost
                    parents[n] = node!!
                }
                processed.plusAssign(node!!)
                node = costs.getLowest(processed)
            }
        }
    }

    fun getPath(start: MatrixIndex, end: MatrixIndex): MutableList<MatrixIndex> {
        val result = mutableListOf<MatrixIndex>()

        var el = parents[end]
        while (el != start) {
            result += el!!
            el = parents[el!!]
        }

        result.reverse()

        return result
    }

    operator fun get(i: MatrixIndex): MutableMap<MatrixIndex, Cost>? {
        return content[i]
    }
}

class SeamCosts() {
    private lateinit var content: MutableMap<MatrixIndex, Cost>

    constructor(graph: SeamGraph) {
        for (i in graph.vertices) {
            content[i] = Cost(Double.POSITIVE_INFINITY)
        }
        content[MatrixIndex(0, 0)] = Cost(0.0)
        for ((k, v) in graph[MatrixIndex(0, 0)]!!) {
            content[k] = v
        }
    }

    fun getLowest(processed: MutableList<MatrixIndex>): MatrixIndex? {
        var minValue = Cost(0.0)
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

class SeamParents() {
    lateinit var content: MutableMap<MatrixIndex, MatrixIndex>

    constructor(graph: SeamGraph) {
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
