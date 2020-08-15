package workers


class InputData(private val input: Array<String>) {
    val actionType: String? = getArgValue(InputFlags.ACTION.flag)
    val inputImagePath: String? = getArgValue(InputFlags.INPUT_FILE_PATH.flag)
    val outputImagePath: String? = getArgValue(InputFlags.OUTPUT_FILE_PATH.flag)
    val verticalSeamsToRemove: Int? = getArgValue(InputFlags.WIDTH_TO_REMOVE.flag)?.toInt()
    val horizontalSeamsToRemove: Int? = getArgValue(InputFlags.HEIGHT_TO_REMOVE.flag)?.toInt()

    private fun getArgValue(arg: String): String? {
        for (i in input.indices) {
            if (input[i] == arg && i + 1 < input.size) {
                return input[i + 1]
            }
        }
        return null
    }
}
