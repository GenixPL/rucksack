package models

class Subset {
    val blocks: MutableList<Block> = mutableListOf()
    var total_value = 0
    var total_area = 0

    constructor(allBlocks: List<Block>, combination: IntArray) {
        combination.forEach {
            var currentBlock = allBlocks[it].copy()
            blocks.add(currentBlock)
            total_value += currentBlock.value
            total_area += currentBlock.area
        }
    }
}