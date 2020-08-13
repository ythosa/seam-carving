package workers

class InputData(private val input: Array<String>) {
    val actionType: String? = getArgValue("-action")
    val inputImagePath: String? = getArgValue("-in")
    val outputImagePath: String? = getArgValue("-out")
    val verticalSeamsToRemove: Int? = getArgValue("-width")?.toInt()
    val horizontalSeamsToRemove: Int? = getArgValue("-height")?.toInt()

    private fun getArgValue(arg: String): String? {
        for (i in input.indices) {
            if (input[i] == arg && i + 1 < input.size) {
                return input[i + 1]
            }
        }
        return null
    }
}
