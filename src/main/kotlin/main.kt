import models.Block
import models.Board
import models.Subset
import untils.CombinationGenerator
import untils.Rucksack
import untils.SubsetChecker
import java.io.File
import java.util.*


const val filepath = "/Users/genix/Projects/rucksack/src/main/kotlin/data.txt"


fun main() {
    Rucksack(filepath).findBest()
}


