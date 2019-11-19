package untils

import models.Subset
import printVerbose
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

        // we can skip those that can't generate better value
        if (subset.totalValue <= threadsManager.getBestValue()) {
            notifyListeners(canFit)
            if (printVerbose) println("thread doesn't check (smaller value)")
            return
        }

        if (printVerbose) println("New thread starts checking permutation: ${subset.blocksToInclude.joinToString()}")

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