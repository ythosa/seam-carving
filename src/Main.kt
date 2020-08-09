package seamcarving

import seamcarving.workers.ActionWorker
import seamcarving.workers.InputWorker

fun main(args: Array<String>) {
    val inputWorker = InputWorker(args)

    val actionType = inputWorker.getActionType()
    val inPath = inputWorker.getInputPath()
    val outPath = inputWorker.getOutputPath()

    ActionWorker(actionType, inPath, outPath).parse()
}
