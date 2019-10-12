package untils

import models.Board
import models.Space
import models.Subset

class SubsetChecker {
    private val subset: Subset
    private val board: Board

    constructor(subset: Subset, board: Board) {
        this.subset = subset
        this.board = board
    }

    fun run() {
        println("SubsetChecker")
        println("Board w:${board.width} h:${board.height}")
        subset.blocks.forEach { println("\tBlock w:${it.width} h:${it.height}") }

        val space = Space(0, 0, board.width, board.height)

        println("fit: " + space.willFit(subset.blocks[0]))
        
        var newSpaces = space.handleFit(subset.blocks[1])
        newSpaces.forEach {
            println("new space: x:${it.posX} y:${it.posY} w:${it.width} h:${it.height}")
        }

//        println("will fit init space")
//        subset.blocks.forEach {
//            println(space.willFit(it))
//        }


    }
}