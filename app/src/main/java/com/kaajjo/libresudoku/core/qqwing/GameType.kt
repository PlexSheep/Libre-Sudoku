package com.kaajjo.libresudoku.core.qqwing

import android.os.Parcelable
import com.kaajjo.libresudoku.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class GameType(
    val size: Int,
    val sectionHeight: Int,
    val sectionWidth: Int,
    val resName: Int,
    val killer: Boolean
) : Parcelable {
    Unspecified(1, 1, 1, R.string.type_unspecified, false),
    Default9x9(9, 3, 3, R.string.type_default_9x9, false),
    Default12x12(12, 3, 4, R.string.type_default_12x12, false),
    Default6x6(6, 2, 3, R.string.type_default_6x6, false),
    Killer9x9(9, 3, 3, R.string.type_default_9x9, true),
    Killer12x12(12, 3, 4, R.string.type_default_12x12, true),
    Killer6x6(6, 2, 3, R.string.type_default_6x6, true),
}