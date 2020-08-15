package workers

enum class InputFlags(val flag: String) {
    ACTION("-action"),
    INPUT_FILE_PATH("-in"),
    OUTPUT_FILE_PATH("-out"),
    WIDTH_TO_REMOVE("-width"),
    HEIGHT_TO_REMOVE("-height"),
}
