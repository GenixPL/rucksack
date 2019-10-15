import untils.Rucksack
import java.io.File
import javax.imageio.ImageIO

//TODO: GUI
const val filepath = "C:/Users/klosi/Desktop/Studies/7th SEMESTER/Algoriths and computability/Project/rucksack/src/main/kotlin/data.txt"
//const val filepath = "/Users/genix/Projects/rucksack/src/main/kotlin/data.txt"

//TODO: GUI
const val outputPath = "C:/Users/klosi/Desktop/Studies/7th SEMESTER/Algoriths and computability/Project/rucksack/src/main/kotlin/result-img.jpg"
//const val outputPath = "/Users/genix/Projects/rucksack/src/main/kotlin/result-img.jpg"

//TODO: GUI print whose 'printlns' that are here and inside 'Rucksack.kt', because they are somehow
// important, and don't cause much of a delay

//TODO: GUI
const val maxThreads = 10

//TODO: GUI
const val maxTime = 120L // seconds

//TODO: GUI
const val imgMultiplier = 10

fun main() {
    val res = Rucksack(filepath).findBest() ?: return

    println()
    println("Best value: ${res.bestValue}")
    println("Best subset: ")
    res.bestSubset.blocks.forEach {
        println(it)
    }

    println("Generating image...")
    val output = File(outputPath)
    // saving result to 'b' forces awaiting for this generating to end
    val b = ImageIO.write(res.bufferedImage, "jpg", output)
    println("Generating done")
}
