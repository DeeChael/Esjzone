package net.deechael.esjzone.novellibrary.novel

import java.io.Serializable

data class Category(
    val name: String,
    val url: String,
    val isAdult: Boolean
) : Serializable
