package net.deechael.esjzone.novellibrary.novel

import java.io.Serializable

interface Novel : Serializable {
    val name: String
    val url: String
}

interface CoveredNovel : Novel {
    val coverUrl: String
    val views: Int
    val likes: Int
    val isAdult: Boolean
}

data class HistoryNovel(
    override val name: String,
    override val url: String,
    val vid: String,
    val chapter: Chapter
) : Novel

data class FavoriteNovel(
    override val name: String,
    override val url: String,
) : Novel

data class CategoryNovel(
    override val name: String,
    override val url: String,
    val forumUrl: String
) : Novel

data class CoveredNovelImpl(
    override val coverUrl: String,
    override val name: String,
    override val url: String,
    override val views: Int,
    override val likes: Int,
    override val isAdult: Boolean
) : CoveredNovel {

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

private val FORUM_URL_REGEX = "/forum/[0-9]+/([0-9]+)/".toRegex()

data class DetailedNovel(
    override val name: String,
    override val url: String,
    override val coverUrl: String,
    override val views: Int,
    override val likes: Int,
    val words: Int,
    val type: String,
    val author: String,
    val forumUrl: String,
    val tags: List<String>,
    override val isAdult: Boolean,
    val isFavorite: Boolean,
    val description: NovelDescription,
    val chapterList: NovelChapterList
) : CoveredNovel {

    fun id(): String {
        return FORUM_URL_REGEX.find(this.forumUrl)!!.groupValues[1]
    }

}