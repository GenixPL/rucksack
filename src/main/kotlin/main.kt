import models.Block
import models.Board
import models.Subset
import untils.CombinationGenerator
import untils.SubsetChecker
import java.io.File
import java.util.*


const val filepath = "/Users/genix/Projects/rucksack/src/main/kotlin/data.txt"


fun main() {
    //read input
    val file = File(filepath)
    val scanner = Scanner(file)

    val wBoard = scanner.nextInt()
    val hBoard = scanner.nextInt()
    val board = Board(wBoard, hBoard)

    val n = scanner.nextInt()
    val blocks = mutableListOf<Block>()
    for (i in 0 until n) {
        val w = scanner.nextInt()
        val h = scanner.nextInt()
        val v = scanner.nextInt()

        //skip those that won't fit
        if (w > wBoard || h > hBoard) {
            continue
        }

        blocks.add(Block(w, h, v))
    }

    blocks.sortByDescending { it.area }

    var bestValue = 0;
    val time1 = System.currentTimeMillis()
    println("ALGORITHM STARTS")

    // go through each possible combination size
    for (k in 1..n) {
        val allCombinations = CombinationGenerator.generate(n, k)

        for (i in allCombinations.indices) {
            val subset = Subset(blocks, allCombinations[i])

            //check subset total area
            if (subset.total_area > board.area) {
                continue
            }

            //check subset total value
            if (subset.total_value < bestValue) {
                continue
            }

            val subsetChecker = SubsetChecker(subset, board)
            val canFit: Boolean =  subsetChecker.run()

            if (canFit) {
//                println("subset ${it.toList()} with value ${subset.total_value} fits!")
                if (subset.total_value > bestValue) {
                    bestValue = subset.total_value
                }
            } else {
//                println("subset ${it.toList()} with value ${subset.total_value} doesn't fit!")
            }
        }
    }

    val time2 = System.currentTimeMillis()
    print("EXECUTION TIME: ${(time2-time1) / 1000} seconds")

    println()
    println("BEST VALUE $bestValue")
}


