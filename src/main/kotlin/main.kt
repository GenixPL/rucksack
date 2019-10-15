import untils.CheckingThread
import untils.Rucksack
import untils.ThreadsManager
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import javax.imageio.ImageIO


//TODO: GUI
const val filepath = "/Users/genix/Projects/rucksack/src/main/kotlin/data.txt"

//TODO: GUI
const val outputPath = "/Users/genix/Projects/rucksack/src/main/kotlin/result-img.jpg"

//TODO: GUI print whose 'printlns' that are here and inside 'Rucksack.kt', because they are somehow
// important, and don't cause much of a delay

const val maxThreads = 10
const val maxTime = 120L // seconds

fun main() {
    val res = Rucksack(filepath).findBest()

    println()
    println("Best value: ${res.bestValue}")
    println("Best subset: ")
    res.bestSubset.blocks.forEach {
        println(it)
    }

    println("Generating image...")
    val output = File(outputPath)
    // saving result to 'b' forces awaiting of this generating
    val b = ImageIO.write(res.bufferedImage, "jpg", output)
    println("Generating done")
}

//fun checkThreads() {
//    val threadsManager = ThreadsManager()
//
//    val executor = Executors.newFixedThreadPool(10) as ThreadPoolExecutor
//
//    for (i in 50 downTo 0) {
//        val newThread = CheckingThread(i * 100L)
//        newThread.addListener(threadsManager)
//        println("Created : " + newThread.id)
//
//        executor.execute(newThread)
//    }
//
//    executor.shutdown()
//}


