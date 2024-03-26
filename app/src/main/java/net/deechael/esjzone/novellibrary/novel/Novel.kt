package net.deechael.esjzone.novellibrary.novel

import java.io.Serializable

interface Novel : Serializable {

    val coverUrl: String
    val name: String
    val url: String
}

data class CoveredNovel(
    override val coverUrl: String,
    override val name: String,
    override val url: String,
    val views: Int,
    val likes: Int,
) : Novel

data class DetailedNovel(
    val name: String,
    val cover: String,
    val views: Int,
    val likes: Int,
    val words: Int,
    val type: String,
    val author: String,
    val description: NovelDescription,
    val chapterList: NovelChapterList
) : Serializable