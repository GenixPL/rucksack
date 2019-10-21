package untils

import imgMultiplier
import models.*
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import kotlin.math.roundToInt

/**
 * Object of this class should be used only once - only one execution will
 * produce a proper result after construction
 */
class ImageGenerator {
    private val subset: Subset
    private val board: Board
    private val possibilities: Stack<PossibilityForImage> = Stack()
    private val mul = imgMultiplier


    constructor(subset: Subset, board: Board) {
        this.board = board
        this.subset = subset.copy()

        val initSpaces = mutableListOf(Space(0, 0, board.width, board.height))
        val initPlacedBlocks = mutableListOf<BlockForImage>()
        possibilities.push(PossibilityForImage(subset.blocks, initSpaces, initPlacedBlocks))
    }

    fun generate(): BufferedImage {
        val blocks = getBlocksForImage()

        return getImg(blocks)
    }

    private fun getImg(blocks: List<BlockForImage>): BufferedImage {
        val img = BufferedImage(board.width * mul, board.height * mul, BufferedImage.TYPE_3BYTE_BGR)

        var maxValue = 0
        this.subset.blocks.forEach {
            if(maxValue < it.value)
                maxValue = it.value
        }

        println("this.subset: " + this.subset.blocks.size)
        for (i in blocks.indices) {
            val g = img.graphics

            val v = this.subset.blocks[i].value.toFloat() / maxValue

            g.color = Color((255 * v).roundToInt(), 0, 0)

            val x = blocks[i].posX * mul
            val y = blocks[i].posY * mul
            val w = blocks[i].width * mul
            val h = blocks[i].height * mul
            g.fillRect(x, y, w, h)
        }

        return img
    }

    private fun getBlocksForImage(): List<BlockForImage> {
        // won't be used, because we will return 'placedBlocks' from given possibility
        val placed = mutableListOf<BlockForImage>()

        while (!possibilities.empty()) {
            val curPossibility = possibilities.pop()

            if (curPossibility.blocks.size == 0) {
                return curPossibility.placedBlocks
            }

            val curBlock = curPossibility.blocks.removeAt(0)

            for (i in 0 until curPossibility.spaces.size) {
                val curSpace = curPossibility.spaces[i]

                handleBlockForSpace(curPossibility, i, curBlock, curSpace)
                handleBlockForSpace(curPossibility, i, curBlock.rotatedCopy(), curSpace)
            }
        }

        // should not reach that point (should return from while)
        return placed
    }

    private fun handleBlockForSpace(
        possibility: PossibilityForImage,
        currentSpaceIndex: Int,
        block: Block,
        space: Space
    ) {
        if (space.canFit(block)) {
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

            val placedBlocks = possibility.copyPlacedBlocks()
            placedBlocks.add(BlockForImage(block.width, block.height, space.posX, space.posY))
            val newPossibility = PossibilityForImage(possibility.copyBlocks(), newSpaces, placedBlocks)
            possibilities.push(newPossibility)
        }
    }
}