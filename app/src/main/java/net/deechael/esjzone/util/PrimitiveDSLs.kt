package net.deechael.esjzone.util

fun String.toHexInt(): Int {
    return this.toInt(16)
}

fun String.toHexUInt(): UInt {
    return this.toUInt(16)
}

fun String.toHexIntOrNull(): Int? {
    return this.toIntOrNull(16)
}

fun String.toHexUIntOrNull(): UInt? {
    return this.toUIntOrNull(16)
}

fun String.removeLeft(length: Int): String {
    return this.substring(length)
}

fun String.removeRight(length: Int): String {
    return this.substring(0, this.length - length)
}

fun String.removeBefore(index: Int, keepIndex: Boolean): String {
    return this.substring(if (keepIndex) index else index + 1)
}
