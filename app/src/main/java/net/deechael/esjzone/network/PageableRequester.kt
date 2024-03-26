package net.deechael.esjzone.network

interface PageableRequester<T> {

    fun pages(): Int

    fun more(): List<T>

    fun more(page: Int): List<T>

    fun end(): Boolean

}