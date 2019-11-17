package untils

import models.Subset
import java.util.concurrent.locks.ReentrantLock

class ThreadsManager : ThreadCompleteListener {

    private val readWriteLock = ReentrantLock()

    @Volatile
    private var bestValue = -1

    @Volatile
    private var bestSubset: Subset? = null

    fun setBestValueAndSubset(subset: Subset) {
        readWriteLock.lock()
        try {
            if (subset.totalValue > bestValue) {
                println("Threads Manager has received new best subset")
                bestValue = subset.totalValue
                bestSubset = subset.copy()
            }
        } finally {
            readWriteLock.unlock()
        }
    }

    fun getBestValue(): Int {
        synchronized(readWriteLock) {
            return bestValue
        }
    }

    fun getBestSubset(): Subset {
        synchronized(readWriteLock) {
            return bestSubset!!
        }
    }

    override fun notifyOfThreadComplete(thread: CheckingThread, canFit: Boolean) {
        if (canFit) {
            setBestValueAndSubset(thread.subset)
        }

        thread.removeListener(this)
        thread.interrupt()
    }

}