package untils

import models.*
import java.util.*

class SubsetChecker {
    private val subset: Subset
    private val board: Board
    private val possibilities: Stack<Possibility> = Stack()

    constructor(subset: Subset, board: Board) {
        this.subset = subset
        this.board = board

        val initSpaces = mutableListOf(Space(0, 0, board.width, board.height))
        possibilities.push(Possibility(subset.blocks, initSpaces))
    }

    fun run(): Boolean {
        println("SubsetChecker run")

        var canFitAllBlocks = false

        while (!possibilities.empty()) {
            val curPossibility = possibilities.pop()

            if (curPossibility.blocks.size == 0) {
                canFitAllBlocks = true
                break
            }

            if (curPossibility.spaces.size == 0) {
                break
            }

            val curBlock = curPossibility.blocks.removeAt(0)
            var canFitBlock = false

            for (i in 0 until curPossibility.spaces.size) {
                //if block fits, put it there, generate new spaces and create new possibilities
                val curSpace = curPossibility.spaces[i]

                if (curSpace.canFit(curBlock)) {
                    canFitBlock = true

                    val newSpaces = mutableListOf<Space>()
                    newSpaces.addAll(curSpace.handleFit(curBlock))

                    for (j in 0 until curPossibility.spaces.size) {
                        if (i == j) {
                            continue
                        }

                        // if current block will intersect with some space, then add new spaces, in oder case
                        // just add the same (untouched) space
                        if (curPossibility.spaces[j].willIntersect(curBlock, curSpace.posX, curSpace.posY)) {
                            newSpaces.addAll(
                                curPossibility.spaces[j].handleIntersection(
                                    curBlock,
                                    curSpace.posX,
                                    curSpace.posY
                                )
                            )
                        } else {
                            newSpaces.add(curPossibility.spaces[j].copy())
                        }
                    }

                    possibilities.push(Possibility(curPossibility.copyBlocks(), newSpaces))
                }

                val rotated = curBlock.rotatedCopy()
                if (curSpace.canFit(rotated)) {
                    canFitBlock = true

                    val newSpaces = mutableListOf<Space>()
                    newSpaces.addAll(curSpace.handleFit(rotated))

                    for (j in 0 until curPossibility.spaces.size) {
                        if (i == j) {
                            continue
                        }

                        // if current rotated block will intersect with some space, then add new spaces, in oder case
                        // just add the same (untouched) space
                        if (curPossibility.spaces[j].willIntersect(rotated, curSpace.posX, curSpace.posY)) {
                            newSpaces.addAll(
                                curPossibility.spaces[j].handleIntersection(
                                    rotated,
                                    curSpace.posX,
                                    curSpace.posY
                                )
                            )
                        } else {
                            newSpaces.add(curPossibility.spaces[j].copy())
                        }
                    }

                    possibilities.push(Possibility(curPossibility.copyBlocks(), newSpaces))
                }
            }

            if (curPossibility.blocks.size == 0 && canFitBlock) {
                canFitAllBlocks = true
                break
            }
        }

        return canFitAllBlocks
    }
}

//        val space = Space(0, 0, 4, 4)
//        val block = Block(3, 3, 1)
//        println("fit: " + space.willFit(block))
//        if (space.willFit(block)) {
//            var newSpaces = space.handleFit(block)
//            newSpaces.forEach {
//                println("new space: x:${it.posX} y:${it.posY} w:${it.width} h:${it.height}")
//            }
//        }


//        val space = Space(2, 2, 4, 4)
//        val block = Block(6, 6, 1)
//        val x = 2
//        val y = 2
//        println("intersect: ${space.willIntersect(block, x, y)}")
//        if (space.willIntersect(block, x, y)) {
//            val newSpaces = space.handleIntersection(block, x, y)
//            println("new spaces")
//            newSpaces.forEach { println("space x${it.posX} y:${it.posY} w:${it.width} h:${it.height}") }
//        }