package untils

import models.Block
import models.Board
import models.Subset
import java.awt.Color
import java.awt.image.BufferedImage
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
        for (k in 1..(blocks.size)) {
            val allCombinations = CombinationGenerator.generate(blocks.size, k)

            for (i in allCombinations.indices) {
                val subset = Subset(blocks, allCombinations[i])

                if (subset.total_area > board.area) { //we can skip those that won't fit
                    continue
                }

                if (subset.total_value < bestValue) { //we can skip those that can't generate better value
                    continue
                }

                val subsetChecker = SubsetChecker(subset, board)
                val canFit: Boolean = subsetChecker.canFitBlocks()

                if (canFit) {
                    if (subset.total_value > bestValue) {
                        bestValue = subset.total_value
                        bestSubset = subset
                    }
                }
            }
        }

        println("Checking done...")
        val time2 = System.currentTimeMillis()
        print("Execution time: ${(time2 - time1) / 1000f} seconds")

        println()
        println("Best value: $bestValue")
        println("Best subset: ${bestSubset!!.blocks.toList()}")

        print("Generating image...")
        generateImage(bestSubset!!)
    }

    private fun generateImage(subset: Subset) {
        var img = BufferedImage(board.width, board.height, BufferedImage.TYPE_BYTE_GRAY)

        val g = img.graphics
        g.color = Color.WHITE
        g.fillRect(0, 0, 5, 5)

        val outputfile = File("/Users/genix/Projects/rucksack/src/main/kotlin/result-img.jpg")
        ImageIO.write(img, "jpg", outputfile)
    }
}