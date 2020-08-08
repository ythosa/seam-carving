package seamcarving.workers

class InputWorker {
    fun getArgValue(args: Array<String>, arg: String): String? {
        for (i in args.indices) {
            if (args[i] == arg && i + 1 < args.size) {
                return args[i + 1]
            }
        }

        return null
    }
}
