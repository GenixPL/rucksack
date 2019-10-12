package untils

import java.util.ArrayList
import kotlin.math.min

class CombinationGenerator {

    private constructor()

    companion object {
        private fun helper(combinations: MutableList<IntArray>, data: IntArray, start: Int, end: Int, index: Int) {
            if (index == data.size) {
                val combination = data.clone()
                combinations.add(combination)
            } else {
                val max = min(end, end + 1 - data.size + index)
                for (i in start..max) {
                    data[index] = i
                    helper(combinations, data, i + 1, end, index + 1)
                }
            }
        }

        fun generate(n: Int, k: Int): List<IntArray> {
            val combinations = ArrayList<IntArray>()
            helper(combinations, IntArray(k), 0, n - 1, 0)
            return combinations
        }
    }
}