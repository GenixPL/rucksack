package models

import java.awt.image.BufferedImage

data class FinalResult(
    val bestValue: Int,
    val bestSubset: Subset,
    val bufferedImage: BufferedImage
)