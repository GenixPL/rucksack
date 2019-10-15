package untils

import models.Subset
import java.util.concurrent.CopyOnWriteArraySet


class CheckingThread(
    private val subsetChecker: SubsetChecker,
    val subset: Subset
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