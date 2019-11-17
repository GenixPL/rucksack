package untils

import jdk.internal.jshell.debug.InternalDebugControl.release
import maxThreads
import java.util.concurrent.Semaphore
import java.util.concurrent.Executor


private class BlockingExecutor private constructor(internal val delegate: Executor) : Executor {

    internal val semaphore: Semaphore = Semaphore(maxThreads)

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
