package net.deechael.esjzone.network.features

import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.AuthorizationCookieJar
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import net.deechael.esjzone.network.EsjzoneXPaths
import net.deechael.esjzone.novellibrary.novel.Category
import net.deechael.esjzone.novellibrary.novel.CategoryNovel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

private val FORUM_URL_REGEX = "/forum/[0-9]+/([0-9]+)/".toRegex()

fun EsjzoneClient.listNovels(
    authorization: Authorization,
    category: Category
): List<CategoryNovel> {
    val httpClient = OkHttpClient.Builder()
        .cookieJar(AuthorizationCookieJar(authorization))
        .build()


    val response = httpClient.newCall(
        Request.Builder()
            .url("${EsjzoneUrls.Home}${category.url}")
            .get()
            .headers(this.headers)
            .build()
    ).execute()

    val responseBody = response.body!!.string()
    response.close()

    val document = Jsoup.parse(responseBody)

    val novels = mutableListOf<CategoryNovel>()

    for (element in EsjzoneXPaths.Forum.Novel.evaluate(document).elements) {
        val forumUrl = element.attr("href")
        novels.add(
            CategoryNovel(
                element.text(),
                "/detail/${FORUM_URL_REGEX.find(forumUrl)!!.groupValues[1]}.html",
                forumUrl
            )
        )
    }

    return novels.toList()
}