package untils

interface ThreadCompleteListener {
    fun notifyOfThreadComplete(thread: CheckingThread, canFit: Boolean)
}