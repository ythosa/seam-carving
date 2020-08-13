package workers

import imageModifiers.ImageModifier
import imageModifiers.converters.*
import imageModifiers.seamcarving.*

class ActionWorker(private val name: String?, private val inPath: String?, private val outPath: String?) {
    private var imageModifier: ImageModifier? = null

    constructor() {
        when (name) {
            "to-energy" -> imageModifier = EnergyConverter()
            "to-negative" -> imageModifier = NegativeConverter()
            "seam-carving" -> imageModifier = SeamCarving()
        }
    }

    fun convert(inPath: String, outPath: String) {
        val imageWorker = ImageWorker(inPath, outPath)
        val image = imageWorker.getImage()

        if (imageModifier == null) throw Error("Invalid action name!")

        imageModifier!!.get(image)

        imageWorker.createImageFile(image)
    }
}
