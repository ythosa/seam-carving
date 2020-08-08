package seamcarving.workers

import seamcarving.coverters.EnergyConverter
import seamcarving.coverters.NegativeConverter

class ActionWorker(private val name: String?, private val inPath: String?, private val outPath: String?) {
    fun start() {
        when (name) {
            null -> help()
            "energy" -> energyConverting()
            "negative" -> negativeConverting()
        }
    }

    private fun help() {
        println("its help")
    }

    private fun energyConverting() {
        val converter = EnergyConverter()
        if (inPath != null && outPath != null) {
            converter.createConverted(inPath, outPath)
        }
    }

    private fun negativeConverting() {
        val converter = NegativeConverter()
        if (inPath != null && outPath != null) {
            converter.createConverted(inPath, outPath)
        }
    }
}
