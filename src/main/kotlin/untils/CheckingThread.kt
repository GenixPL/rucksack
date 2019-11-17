package untils

import models.Subset
import java.util.concurrent.CopyOnWriteArraySet


class CheckingThread(
    private val subsetChecker: SubsetChecker,
    val subset: Subset,
    private val threadsManager: ThreadsManager
) : Thread() {

    private val listeners = CopyOnWriteArraySet<ThreadCompleteListener>()

    fun addListener(listener: ThreadCompleteListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ThreadCompleteListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners(canFit: Boolean) {
        for (listener in listeners) {
            listener.notifyOfThreadComplete(this, canFit)
        }
    }

    override fun run() {
        var canFit = false

        // we can skip those that won't fit
        if (subset.totalArea > subsetChecker.board.area) {
            notifyListeners(canFit)
//            println("thread doesn't check")
            return
        }

        // we can skip those that can't generate better value
        if (subset.totalValue <= threadsManager.getBestValue()) {
            notifyListeners(canFit)
//            println("thread doesn't check")
            return
        }

        println("New threads starts checking permutation: ${subset.permutation.joinToString()}")

        try {
            canFit = doRun()
        } finally {
            notifyListeners(canFit)
        }
    }

    private fun doRun(): Boolean {
        return subsetChecker.canFitBlocks()
    }
}