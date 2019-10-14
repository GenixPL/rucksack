package models

data class PossibilityForImage(
    val blocks: MutableList<Block>,
    val spaces: MutableList<Space>,
    val placedBlocks: MutableList<BlockForImage>
) {
    fun copyBlocks(): MutableList<Block> {
        return blocks.toMutableList()
    }

    fun copyPlacedBlocks(): MutableList<BlockForImage> {
        return placedBlocks.toMutableList()
    }
}