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
    val isAdult: Boolean
) : Novel {

    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false
        if (this === other)
            return true
        if (other !is Novel)
            return false
        return other.url.contentEquals(this.url)
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }

}

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