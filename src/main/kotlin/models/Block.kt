package models

data class Block(
    val width: Int,
    val height: Int,
    val value: Int
) {
    val area = width * height

    fun rotatedCopy(): Block {
        return Block(height, width, value)
    }

    fun isSquare(): Boolean {
        return (width == height)
    }
}