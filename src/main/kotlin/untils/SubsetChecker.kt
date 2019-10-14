package untils

import models.*
import java.util.*

/**
 * Object of this class should be used only once - only one execution of 'canFitBlocks' will
 * produce a proper result after construction
 */
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

    fun canFitBlocks(): Boolean {
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
                val curSpace = curPossibility.spaces[i]

                val res1 = handleBlockForSpace(curPossibility, i, curBlock, curSpace)
                val res2 = handleBlockForSpace(curPossibility, i, curBlock.rotatedCopy(), curSpace)

                canFitBlock = (res1 || res2)
            }

            if (curPossibility.blocks.size == 0 && canFitBlock) {
                canFitAllBlocks = true
                break
            }
        }

        return canFitAllBlocks
    }

    private fun handleBlockForSpace(
        possibility: Possibility,
        currentSpaceIndex: Int,
        block: Block,
        space: Space
    ): Boolean {
        var canFitBlock = false

        if (space.canFit(block)) {
            canFitBlock = true

            val newSpaces = mutableListOf<Space>()
            newSpaces.addAll(space.handleFit(block))

            for (j in 0 until possibility.spaces.size) {
                if (currentSpaceIndex == j) {
                    continue
                }

                // if current block will intersect with some space, then add new spaces, in oder case
                // just add the same (untouched) space
                if (possibility.spaces[j].willIntersect(block, space.posX, space.posY)) {
                    newSpaces.addAll(
                        possibility.spaces[j].handleIntersection(
                            block,
                            space.posX,
                            space.posY
                        )
                    )
                } else {
                    newSpaces.add(possibility.spaces[j].copy())
                }
            }

            possibilities.push(Possibility(possibility.copyBlocks(), newSpaces))
        }

        return canFitBlock
    }
}

