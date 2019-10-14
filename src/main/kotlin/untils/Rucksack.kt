package untils

import models.Block
import models.Board
import models.Subset
import java.io.File
import java.util.*
import javax.imageio.ImageIO


class Rucksack {
    private val board: Board
    private val blocks = mutableListOf<Block>()

    private var bestValue = -1
    private var bestSubset: Subset? = null

    constructor(pathToData: String) {
        //read input
        val file = File(pathToData)
        val scanner = Scanner(file)

        val wBoard = scanner.nextInt()
        val hBoard = scanner.nextInt()
        this.board = Board(wBoard, hBoard)

        val n = scanner.nextInt()
        for (i in 0 until n) {
            val w = scanner.nextInt()
            val h = scanner.nextInt()
            val v = scanner.nextInt()

            //skip those that won't fit
            if (w > wBoard || h > hBoard) {
                continue
            }

            this.blocks.add(Block(w, h, v))
        }

        blocks.sortByDescending { it.area }
    }

    fun findBest() {
        val time1 = System.currentTimeMillis()
        println("Checking starts...")

        // go through each possible subset size

        // checking from biggest combinations may reduce number of needed executions, since bigger
        // sets are more probable to contain bigger totalValues, but we still have to check all possibilities
        for (k in (blocks.size) downTo 1) {
            val allCombinationsForK = CombinationGenerator.generate(blocks.size, k)
            val allSubsetsForK = mutableListOf<Subset>()
            allCombinationsForK.forEach {
                allSubsetsForK.add(Subset(blocks, it))
            }

            // check them from the biggest value, which will significantly reduce number of executions
            allSubsetsForK.sortByDescending { it.totalValue }

            for (i in allSubsetsForK.indices) {
                val subset = allSubsetsForK[i].copy()

                if (subset.totalArea > board.area) { //we can skip those that won't fit
                    continue
                }

                if (subset.totalValue < bestValue) { //we can skip those that can't generate better value
                    continue
                }

                val subsetChecker = SubsetChecker(subset, board)
                val canFit: Boolean = subsetChecker.canFitBlocks()

                if (canFit) {
                    if (subset.totalValue > bestValue) {
                        bestValue = subset.totalValue
                        //we have to create a new one, because SubsetChecker changes previous one
                        bestSubset = allSubsetsForK[i]
                    }
                }
            }
        }

        println("Checking done")
        val time2 = System.currentTimeMillis()
        print("Execution time: ${(time2 - time1) / 1000f} seconds")

        println()
        println("Best value: $bestValue")
        println("Best subset: ")
        bestSubset!!.blocks.forEach {
            println(it)
        }

        generateImage(bestSubset!!)
    }

    private fun generateImage(subset: Subset) {
        println("Generating image...")
        val img = ImageGenerator(subset, board).generate()

        val output = File("/Users/genix/Projects/rucksack/src/main/kotlin/result-img.jpg")
        val b = ImageIO.write(img, "jpg", output)
        println("Generating done")
    }
}