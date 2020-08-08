package seamcarving.workers

class InputWorker(private val input: Array<String>) {
    private fun getArgValue(arg: String): String? {
        for (i in input.indices) {
            if (input[i] == arg && i + 1 < input.size) {
                return input[i + 1]
            }
        }
        return null
    }

    fun getActionType(): String? {
        return getArgValue("-action")
    }

    fun getInputPath(): String? {
        return getArgValue("-in")
    }

    fun getOutputPath(): String? {
        return getArgValue("-out")
    }
}
