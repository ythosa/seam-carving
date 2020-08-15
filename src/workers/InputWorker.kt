package workers


class InputWorker(private val input: Array<String>) {
    private val inputData = InputData(input)

    fun parse() {
        when (inputData.actionType) {
            null -> help()
            "help" -> help()
            else -> ActionWorker(inputData).convert()
        }
    }

    private fun help() {
        println("Welcome to SeamCarving. Supported flags:")
        for (flag in InputFlags.values()) {
            println(flag.getFlagInfo())
        }
    }
}
