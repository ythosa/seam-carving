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

    private fun wrongUsing(name: String, error: String? = null) {
        println("Wrong using of action $name.")
        if (!error.isNullOrEmpty()) {
            println(error)
        }
        println("Get more info with help action.")
    }

    private fun help() {
        println("Welcome to SeamCarving.\n" +
                "Supported flags:\n" +
                "\t-action //- type of work with image or something else, which could be:\n" +
                "\t\t+ help //- get help for this util\n" +
                "\t\t+ to-energy //- convert image from -in path to -out path with energy filter\n" +
                "\t\t+ to-negative //- convert image from -in path to -out path with negative filter\n" +
                "\t\t+ seam-carving //- seam-carving -width & -height image from -in path to -out path\n" +
                "\t-in //- input path of existing image\n" +
                "\t-out //- output path of created image\n" +
                "\t-width //- width to remove with seam-carving\n" +
                "\t-height //- height to remove with seam-carving")
    }
}
