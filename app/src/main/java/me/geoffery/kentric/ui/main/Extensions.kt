package me.geoffery.kentric.ui.main

inline fun <T> Sequence<T>.forEachIndexedCaped(cap:Int? = null, action: (index: Int, T) -> Unit): Unit {
    var index = 0
    for (item in this) {
        if (cap != null && index >= cap) break
        action(index++, item)
    }
}