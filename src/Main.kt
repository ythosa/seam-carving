package seamcarving

import seamcarving.workers.ActionWorker
import seamcarving.workers.InputWorker

fun main(args: Array<String>) {
    val inputWorker = InputWorker()

    val actionType = inputWorker.getArgValue(args, "-action")
    val inPath = inputWorker.getArgValue(args, "-in")
    val outPath = inputWorker.getArgValue(args, "-out")

    ActionWorker(actionType, inPath, outPath)
}
