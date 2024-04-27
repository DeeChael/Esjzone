package net.deechael.esjzone.network.features

import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.AuthorizationCookieJar
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import net.deechael.esjzone.network.EsjzoneXPaths
import net.deechael.esjzone.network.PageableRequester
import net.deechael.esjzone.novellibrary.novel.FavoriteNovel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

fun EsjzoneClient.getFavorites(
    authorization: Authorization,
    sort: String
): Pair<PageableRequester<FavoriteNovel>, List<FavoriteNovel>> {
    val httpClient = OkHttpClient.Builder()
        .cookieJar(AuthorizationCookieJar(authorization))
        .build()

    val response = httpClient.newCall(
        Request.Builder()
            .url("${EsjzoneUrls.My.Favorite}/$sort/1")
            .get()
            .headers(this.headers)
            .build()
    ).execute()

    val responseBody = response.body!!.string()
    response.close()


    val document = Jsoup.parse(responseBody)

    val matcher = pagesRegex.find(EsjzoneXPaths.Profile.Favorite.Pages.evaluate(document).get())

    val pages = matcher?.groupValues?.get(1)?.toInt() ?: 0

    val novels = mutableListOf<FavoriteNovel>()

    for (novelData in EsjzoneXPaths.Profile.Favorite.Novel.evaluate(document).elements) {
        novels.add(
            FavoriteNovel(
                novelData.text(),
                novelData.attr("href")
            )
        )
    }

    return FavoriteNovelRequester(authorization, sort, pages) to novels.toList()
}


private class FavoriteNovelRequester(
    authorization: Authorization,
    private val sort: String,
    private val pages: Int
) : PageableRequester<FavoriteNovel> {

    private val httpClient = OkHttpClient.Builder()
        .cookieJar(AuthorizationCookieJar(authorization))
        .build()

    private var current: Int = 2
    override fun pages(): Int {
        return this.pages
    }

    override fun more(): List<FavoriteNovel> {
        val more = this.more(this.current)
        current += 1
        return more
    }

    override fun more(page: Int): List<FavoriteNovel> {
        val response = httpClient.newCall(
            Request.Builder()
                .url("${EsjzoneUrls.My.Favorite}/$sort/$page")
                .get()
                .headers(EsjzoneClient.headers)
                .build()
        ).execute()

        val responseBody = response.body!!.string()
        response.close()

        val document = Jsoup.parse(responseBody)

        val novels = mutableListOf<FavoriteNovel>()

        for (novelData in EsjzoneXPaths.Profile.Favorite.Novel.evaluate(document).elements) {
            novels.add(
                FavoriteNovel(
                    novelData.text(),
                    novelData.attr("href")
                )
            )
        }

        return novels.toList()
    }

    override fun end(): Boolean {
        return current > pages
    }

}