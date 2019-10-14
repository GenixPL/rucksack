import untils.Rucksack
import java.io.File
import javax.imageio.ImageIO

//TODO: GUI
const val filepath = "/Users/genix/Projects/rucksack/src/main/kotlin/data.txt"

//TODO: GUI
const val outputPath = "/Users/genix/Projects/rucksack/src/main/kotlin/result-img.jpg"

//TODO: GUI print whose 'printlns' that are here and inside 'Rucksack.kt', because they are somehow
// important, and don't cause much of a delay

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
    val b = ImageIO.write(res.bufferedImage, "jpg", output)
    println("Generating done")
}


