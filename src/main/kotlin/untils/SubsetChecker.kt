package untils

import models.Block
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