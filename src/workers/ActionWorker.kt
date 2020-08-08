package seamcarving.workers

import seamcarving.coverters.EnergyConverter
import seamcarving.coverters.NegativeConverter

class ActionWorker(private val name: String?, private val inPath: String?, private val outPath: String?) {
    fun parse() {
        when (name) {
            null -> help()
            "help" -> help()
            "energy" -> energyConverting()
            "negative" -> negativeConverting()
            else -> wrongUsing(name)
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
                "\t\t+ energy //- convert image from -in path to -out path with energy filter\n" +
                "\t\t+ negative //- convert image from -in path to -out path with negative filter\n" +
                "\t\t+ crossed-rec //- create crossed rectangle image\n" +
                "\t-in //- input path of existing image\n" +
                "\t-out //- output path of created image")
    }

    private fun energyConverting() {
        if (inPath != null && outPath != null) {
            val converter = EnergyConverter()
            converter.createConverted(inPath, outPath)
        } else {
            wrongUsing("energy", "Invalid passed -in and/or -out flags. ")
        }
    }

    private fun negativeConverting() {
        if (inPath != null && outPath != null) {
            val converter = NegativeConverter()
            converter.createConverted(inPath, outPath)
        } else {
            wrongUsing("energy", "Invalid passed -in and/or -out flags. ")
        }
    }
}
