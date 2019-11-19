package models

data class Subset(
    private val allBlocks: List<Block>,
    val blocksToInclude: IntArray
) {
    val blocks: MutableList<Block> = mutableListOf()
    var totalValue = 0
    var totalArea = 0

    init {
        blocksToInclude.forEach {
            var currentBlock = allBlocks[it].copy()
            blocks.add(currentBlock)
            totalValue += currentBlock.value
            totalArea += currentBlock.area
        }
    }
}