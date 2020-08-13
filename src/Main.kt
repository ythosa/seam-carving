import workers.ActionWorker
import workers.InputWorker

fun main(args: Array<String>) {
    val inputWorker = InputWorker(args).parse()
}
