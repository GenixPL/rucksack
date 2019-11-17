package untils

import maxThreads
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


class Rucksack(pathToData: String) {
    private val board: Board
    private val blocks = mutableListOf<Block>()

    private var bestValue = -1
    private var bestSubset: Subset? = null

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
        val threadsManager = ThreadsManager()
        val executor = Executors.newFixedThreadPool(maxThreads) as ThreadPoolExecutor

        println("Checking starts...")
        val time1 = System.currentTimeMillis()

        // GENERATE COMBINATIONS - INIT
        val size: Double = blocks.size.toDouble();
        val powerSetSize: Long = (2.0.pow(size)).toLong()
        var counter: Int = 0
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

            println("combination: $combination")

            val currentSubset = Subset(blocks, combination.toIntArray())

            
            println()
        }


        return null

//        for (k in (blocks.size) downTo 1) {
//            println("Checking subsets of size $k")
//            val allCombinationsForK = CombinationGenerator.generate(blocks.size, k)
//            println("\t number of combinations to check for this size: ${allCombinationsForK.size}")
//            val allSubsetsForK = mutableListOf<Subset>()
//            allCombinationsForK.forEach {
////                allSubsetsForK.add(Subset(blocks, it))
//            }
//
//            // check them from the biggest value, which will (in most cases)
//            // significantly reduce number of needed executions
//            allSubsetsForK.sortByDescending { it.totalValue }
//
//            for (i in allSubsetsForK.indices) {
//                val subset = allSubsetsForK[i].copy()
//
//                // we can skip those that won't fit
//                if (subset.totalArea > board.area) {
//                    continue
//                }
//
//                // we can skip those that can't generate better value
//                if (subset.totalValue < threadsManager.getBestValue()) {
//                    continue
//                }
//
//                val newThread = CheckingThread(SubsetChecker(subset, board), subset)
//                newThread.addListener(threadsManager)
//                executor.execute(newThread)
//            }
//        }
//
//        executor.shutdown()
//        val finished = executor.awaitTermination(maxTime, TimeUnit.SECONDS)
//        if (!finished) {
//            println("Program will execute for more than a $maxTime seconds (terminating)")
//            return null
//        }
//
//        println("Checking done")
//        val time2 = System.currentTimeMillis()
//        println("Execution time: ${(time2 - time1) / 1000f} seconds")
//
//        return FinalResult(
//            threadsManager.getBestValue(),
//            threadsManager.getBestSubset(),
//            ImageGenerator(threadsManager.getBestSubset().copy(), board).generate()
//        )
    }

    private fun permute(list: List<Int>, l: Int, r: Int) {
        var str = list.toMutableList()
        if (l == r)
//            println(str)
        else {
            for (i in l..r) {
                str[l] = str[i].also { str[i] = str[l] }
                permute(str, l + 1, r)
                str[l] = str[i].also { str[i] = str[l] }
            }
        }
    }

//    fun <T> permute(input: List<T>): List<List<T>> {
//        if (input.size == 1) return listOf(input)
//        val perms = mutableListOf<List<T>>()
//        val toInsert = input[0]
//        for (perm in permute(input.drop(1))) {
//            for (i in 0..perm.size) {
//                val newPerm = perm.toMutableList()
//                newPerm.add(i, toInsert)
//                perms.add(newPerm)
//            }
//        }
//        return perms
//    }
}