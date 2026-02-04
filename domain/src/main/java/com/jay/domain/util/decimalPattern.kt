package com.jay.domain.util

fun String.decimalPatternCustom(): String {
    val decimalCount = this.length - 1 - this.indexOf(".")
    val decimalPattern = StringBuilder()
    var i = 0
    while (i < decimalCount && i < 2) {
        decimalPattern.append("0")
        i++
    }
    return decimalPattern.toString()
}
