package net.deechael.esjzone.novellibrary.novel

import net.deechael.esjzone.novellibrary.component.Component
import java.io.Serializable

data class Chapter(
    val name: String,
    val url: String
) : Serializable

data class DetailedChapter(
    val name: String,
    val content: List<Component>
) : Serializable