package io.github.beecho01.wallpanelreborn.ext


fun String.convertStringToArray(str: String): Array<String> {
    val strSeparator = ","
    return str.split(strSeparator).toTypedArray()
}
