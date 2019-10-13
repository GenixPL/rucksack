package models

data class Possibility(
    val blocks: MutableList<Block>,
    val spaces: MutableList<Space>
) {
    fun copyBlocks(): MutableList<Block> {
        return blocks.toMutableList()
    }

    fun copySpaces(): MutableList<Space> {
        return spaces.toMutableList()
    }
}