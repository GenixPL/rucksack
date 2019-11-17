package untils

import maxThreads
import java.util.concurrent.Semaphore
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor


class BlockingExecutor constructor(val delegate: ThreadPoolExecutor) : Executor {

    private val semaphore: Semaphore = Semaphore(maxThreads)

    override fun execute(command: Runnable) {
        try {
            semaphore.acquire()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return
        }

        val wrapped = {
            try {
                command.run()
            } finally {
                semaphore.release()
            }
        }

        delegate.execute(wrapped)

    }
}
