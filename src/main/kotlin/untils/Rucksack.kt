package untils

import maxThreads
import maxTime
import models.Block
import models.Board
import models.FinalResult
import models.Subset
import java.io.File
import java.util.*
import kotlin.math.pow
import java.util.ArrayList
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class Rucksack(pathToData: String) {
    private val board: Board
    private val blocks = mutableListOf<Block>()

    private val threadsManager = ThreadsManager()
    private val executor = Executors.newFixedThreadPool(maxThreads) as ThreadPoolExecutor

    init {
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
            val fit: Boolean = (w <= wBoard && h <= hBoard) || (w <= hBoard && h <= wBoard)
            if (!fit) {
                continue
            }

            this.blocks.add(Block(w, h, v))
        }
    }

    fun findBest(): FinalResult? {
        println()
        println()
        println("Checking starts...")
        val time1 = System.currentTimeMillis()

        // GENERATE COMBINATIONS - INIT
        val size: Double = blocks.size.toDouble()
        val powerSetSize: Long = (2.0.pow(size)).toLong()
        var counter = 0
        var j: Int

        /*Run from counter 000..0 to 111..1*/
        while (counter < powerSetSize) {
            val combination = ArrayList<Int>()

            // GENERATE COMBINATION - START
            j = 0
            while (j < size) {
                /* Check if jth bit in the counter is set If set then print jth element from set */
                if (counter and (1 shl j) > 0) {
                    combination.add(j)
                }
                j++
            }
            counter++
            // GENERATE COMBINATION - END

            if (combination.isEmpty()) {
                continue
            }

            permute(combination, 0, combination.size - 1)
        }

        executor.shutdown()
        val finished = executor.awaitTermination(maxTime, TimeUnit.SECONDS)
        if (!finished) {
            println("Program terminates due to time limit ($maxTime)")
            return null
        }

        println("Checking done")
        val time2 = System.currentTimeMillis()

        println("Checking was done in: ${(time2 - time1) / 1000f} seconds")

        return FinalResult(
            threadsManager.getBestValue(),
            threadsManager.getBestSubset(),
            ImageGenerator(threadsManager.getBestSubset().copy(), board).generate()
        )
    }

    private fun permute(list: List<Int>, l: Int, r: Int) {
        val currentPermutation = list.toMutableList()

        if (l == r) { // PERMUTATION IS DONE
            val currentSubset = Subset(blocks, currentPermutation.toIntArray())

            // we can skip those that won't fit
            if (currentSubset.totalArea > board.area) {
                return
            }

            // we can skip those that can't generate better value
            if (currentSubset.totalValue < threadsManager.getBestValue()) {
                return
            }

            val newThread = CheckingThread(SubsetChecker(currentSubset.copy(), board), currentSubset.copy())
            newThread.addListener(threadsManager)
            executor.execute(newThread)

        } else { // GENERATE FURTHER
            for (i in l..r) {
                currentPermutation[l] = currentPermutation[i].also { currentPermutation[i] = currentPermutation[l] }
                permute(currentPermutation, l + 1, r)
                currentPermutation[l] = currentPermutation[i].also { currentPermutation[i] = currentPermutation[l] }
            }
        }
    }
}