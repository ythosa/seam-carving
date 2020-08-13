package workers

import imageModifiers.ImageModifier
import imageModifiers.converters.*
import imageModifiers.seamcarving.*

class ActionWorker(private val data: InputData) {
    private var imageModifier: ImageModifier? = null

    constructor() {
        val imageWorker = ImageWorker(data.inputImagePath!!, data.outputImagePath!!)
        val image = imageWorker.getImage()

        when (data.actionType) {
            "to-energy" -> imageModifier = EnergyConverter(image, data)
            "to-negative" -> imageModifier = NegativeConverter(image, data)
            "seam-carving" -> imageModifier = SeamCarving(image, data)
        }
    }

    fun convert(inPath: String, outPath: String) {
        val imageWorker = ImageWorker(inPath, outPath)

        if (imageModifier == null) throw Error("Invalid action name!")

        imageWorker.createImageFile(imageModifier!!.get())
    }
}
