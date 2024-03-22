package net.deechael.esjzone.novellibrary.novel

import java.io.Serializable

data class HomeNovel(
    val coverUrl: String,
    val name: String,
    val url: String,
    val views: Int,
    val likes: Int,
) : Serializable

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