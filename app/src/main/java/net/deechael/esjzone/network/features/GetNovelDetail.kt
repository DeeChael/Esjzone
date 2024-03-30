package net.deechael.esjzone.network.features

import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.AuthorizationCookieJar
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import net.deechael.esjzone.network.EsjzoneXPaths
import net.deechael.esjzone.novellibrary.novel.DetailedNovel
import net.deechael.esjzone.novellibrary.novel.Novel
import net.deechael.esjzone.novellibrary.novel.NovelChapterList
import net.deechael.esjzone.novellibrary.novel.NovelDescription
import net.deechael.esjzone.novellibrary.novel.analyseChapterList
import net.deechael.esjzone.novellibrary.novel.analyseDescription
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup


fun EsjzoneClient.getNovelDetail(authorization: Authorization, novel: Novel): DetailedNovel {
    val httpClient = OkHttpClient.Builder()
        .cookieJar(AuthorizationCookieJar(authorization))
        .build()


    val response = httpClient.newCall(
        Request.Builder()
            .url("${EsjzoneUrls.Home}${novel.url}")
            .get()
            .headers(this.headers)
            .build()
    ).execute()

    val responseBody = response.body!!.string()
    response.close()

    val document = Jsoup.parse(responseBody)

    val coverXPathResult = EsjzoneXPaths.Detail.Cover.evaluate(document).list()

    val coverUrl = if (coverXPathResult.isNotEmpty())
        EsjzoneXPaths.Detail.Cover.evaluate(document).get()
    else
        EsjzoneUrls.EmptyCover

    val views = EsjzoneXPaths.Detail.Views.evaluate(document).get().toInt()
    val likes = EsjzoneXPaths.Detail.Likes.evaluate(document).get().toInt()
    val words = EsjzoneXPaths.Detail.Words.evaluate(document).get().replace(",", "").toInt()

    val type = EsjzoneXPaths.Detail.Type.evaluate(document).get()
    val author = EsjzoneXPaths.Detail.Author.evaluate(document).get()

    val forumUrl = EsjzoneXPaths.Detail.ForumUrl.evaluate(document).get()

    val tags = EsjzoneXPaths.Detail.Tags.evaluate(document).list().toList()

    val descriptionElements = EsjzoneXPaths.Detail.Description.evaluate(document).elements
    val chapterListElements = EsjzoneXPaths.Detail.ChapterList.evaluate(document).elements

    val description = if (descriptionElements.size == 0)
        NovelDescription(listOf())
    else
        analyseDescription(descriptionElements[0])

    val chapterList = if (chapterListElements.size == 0)
        NovelChapterList(listOf())
    else
        analyseChapterList(chapterListElements[0])

    return DetailedNovel(
        novel.name,
        coverUrl,
        views,
        likes,
        words,
        type,
        author,
        forumUrl,
        tags,
        tags.contains("R18"),
        description,
        chapterList
    )
}