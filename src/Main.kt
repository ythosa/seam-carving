package seamcarving

import seamcarving.workers.InputWorker

fun main(args: Array<String>) {
    val inputWorker = InputWorker()
    val inName = inputWorker.getArgValue(args, "-in")
    val outName = inputWorker.getArgValue(args, "-out")
}
