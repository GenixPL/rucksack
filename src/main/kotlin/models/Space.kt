package models

data class Space(
    val posX: Int,
    val posY: Int,
    val width: Int,
    val height: Int
) {
    fun willFit(block: Block): Boolean {
        return (block.width <= width) && (block.height <= height)
    }

    fun handleFit(block: Block): List<Space> {
        if (block.height == height) {
            // we have only one new space to the right of this
            var space = Space(posX + block.width, posY, width - block.width, height)

            return listOf(space)

        } else {
            // we have to new spaces, one to the right of this and second on top of this
            var space1 = Space(posX + block.width, posY, width - block.width, height)
            var space2 = Space(posX, posY + block.height, width, height - block.height)

            return listOf(space1, space2)
        }
    }

    fun willIntersect(block: Block, x: Int, y: Int): Boolean {
        val sxStart: Int = posX
        val sxEnd: Int = posX + width
        val syStart: Int = posY
        val syEnd: Int = posY + height

        val bxStart: Int = x
        val bxEnd: Int = x + block.width
        val byStart: Int = y
        val byEnd: Int = y + block.height

        val xIntersect: Boolean = (bxStart < sxEnd) && (bxEnd > sxStart)
        val yIntersect: Boolean = (byStart < syEnd) && (byEnd > syStart)

        return xIntersect && yIntersect
    }

    fun handleIntersection(block: Block, x: Int, y: Int) {
        //TODO:
    }
}