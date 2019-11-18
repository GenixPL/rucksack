package models

data class Space(
    val posX: Int,
    val posY: Int,
    val width: Int,
    val height: Int
) {
    fun getArea(): Int {
        return width * height
    }

    fun doesContain(otherSpace: Space): Boolean {
        return (otherSpace.posX + otherSpace.width) <= (posX + width) &&
                (otherSpace.posX) >= (posX) &&
                (otherSpace.posY) >= (posY) &&
                (otherSpace.posY + otherSpace.height) <= (posY + height)
    }

    //

    fun canFit(block: Block): Boolean {
        return (block.width <= width) && (block.height <= height)
    }

    fun handleFit(block: Block): List<Space> {
        if ((block.height == height) && (block.width == width)) {
            // block covers whole space
            return listOf()

        } else if (block.height == height) {
            // we have only one new space to the right of this
            var space = Space(posX + block.width, posY, width - block.width, height)

            return listOf(space)

        } else {
            // we have two new spaces, one to the right of this and second on top of this
            var space1 = Space(posX + block.width, posY, width - block.width, height)
            var space2 = Space(posX, posY + block.height, width, height - block.height)

            return listOf(space1, space2)
        }
    }

    //

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

    fun handleIntersection(block: Block, x: Int, y: Int): List<Space> {
        // intersection can produce at most 4 new spaces, which are created when distances listed below
        // have positive value

        val leftDist = x - posX
        val rightDist = (posX + width) - (x + block.width)
        val downDist = y - posY
        val upDist = (posY + height) - (y + block.height)

        val spaces = mutableListOf<Space>()

        if (leftDist > 0) {
            spaces.add(Space(posX, posY, leftDist, height))
        }

        if (rightDist > 0) {
            spaces.add(Space(x + block.width, posY, rightDist, height))
        }

        if (downDist > 0) {
            spaces.add(Space(posX, posY, width, downDist))
        }

        if (upDist > 0) {
            spaces.add(Space(posX, y + block.height, width, upDist))
        }

        return spaces
    }
}