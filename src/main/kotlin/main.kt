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

    //go through each possible combination size
//    for (k in 1..n) {
//        val allCombinations = CombinationGenerator.generate(n, k)

        val combinations = CombinationGenerator.generate(n, n)
        val subset = Subset(blocks, combinations[0])
        val subsetChecker = SubsetChecker(subset, board)
        println("can fit all: ${subsetChecker.run()}")

//        allCombinations.forEach {
//            val subset = Subset(blocks, it)
//
//            //check subset total area
//
//            //check subset total value
//
//            val subsetChecker = SubsetChecker(subset, board)
//            subsetChecker.run()
//        }
//    }

}


