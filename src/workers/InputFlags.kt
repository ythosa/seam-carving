package workers

enum class InputFlags(val flag: String, private val info: String) {
    ACTION("-action", "type of work with image or something else, which could be:\n" +
            "\t+ help //- get help for this util\n" +
            "\t+ to-energy //- convert image from -in path to -out path with energy filter\n" +
            "\t+ to-negative //- convert image from -in path to -out path with negative filter\n" +
            "\t+ seam-carving //- seam-carving -width & -height image from -in path to -out path"),
    INPUT_FILE_PATH("-in", "input path of existing image"),
    OUTPUT_FILE_PATH("-out", "output path of created image"),
    WIDTH_TO_REMOVE("-width", "width to remove with seam-carving"),
    HEIGHT_TO_REMOVE("-height", "height to remove with seam-carving");

    fun getFlagInfo(): String {
        return "$flag //- $info"
    }
}
