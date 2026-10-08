package io.github.beecho01.wallpanelreborn.ext

fun Array<String>.convertArrayToString(): String {
    val strSeparator = ","
    val str = StringBuilder()
    for (i in this.indices) {
        str.append(this[i])
        // Do not append comma at the end of last element
        if (i < this.size - 1) {
            str.append(strSeparator)
        }
    }
    return str.toString()
}
