package models

data class Subset(
    private val allBlocks: List<Block>,
    private val combination: IntArray
) {
    val blocks: MutableList<Block> = mutableListOf()
    var totalValue = 0
    var totalArea = 0

    init {
        combination.forEach {
            var currentBlock = allBlocks[it].copy()
            blocks.add(currentBlock)
            totalValue += currentBlock.value
            totalArea += currentBlock.area
        }
    }
}