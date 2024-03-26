package net.deechael.esjzone.network.features

import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import net.deechael.esjzone.network.EsjzoneXPaths
import net.deechael.esjzone.network.PageableRequester
import net.deechael.esjzone.novellibrary.novel.CoveredNovel
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

private val pagesRegex = "total: ([0-9]+)".toRegex()

fun EsjzoneClient.search(
    authorization: Authorization,
    keyword: String
): Pair<PageableRequester<CoveredNovel>, List<CoveredNovel>> {
    val httpClient = OkHttpClient.Builder()
        .cookieJar(object : CookieJar {
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return listOf(
                    Cookie.Builder().domain("www.esjzone.me").name("ews_key")
                        .value(authorization.ewsKey).build(),
                    Cookie.Builder().domain("www.esjzone.me").name("ews_token")
                        .value(authorization.ewsToken).build()
                )
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            }

        })
        .build()

    val response = httpClient.newCall(
        Request.Builder()
            .url("${EsjzoneUrls.Tags}/$keyword")
            .get()
            .headers(this.headers)
            .build()
    ).execute()

    val responseBody = response.body!!.string()
    response.close()

    val document = Jsoup.parse(responseBody)

    val pages =
        pagesRegex.find(EsjzoneXPaths.Tags.Pages.evaluate(document).get())!!.groupValues[1].toInt()

    val novels = mutableListOf<CoveredNovel>()

    for (novelData in EsjzoneXPaths.Tags.Novel.All.evaluate(document).elements) {
        novels.add(
            CoveredNovel(
                EsjzoneXPaths.Tags.Novel.Cover.evaluate(novelData).get(),
                EsjzoneXPaths.Tags.Novel.Name.evaluate(novelData).get(),
                EsjzoneXPaths.Tags.Novel.Url.evaluate(novelData).get(),
                EsjzoneXPaths.Tags.Novel.Views.evaluate(novelData).get().substring(1).toInt(),
                EsjzoneXPaths.Tags.Novel.Likes.evaluate(novelData).get().substring(1).toInt(),
            )
        )
    }

    return NovelRequester(authorization, keyword, pages) to novels
}

private class NovelRequester(
    private val authorization: Authorization,
    private val keyword: String,
    private val pages: Int
) : PageableRequester<CoveredNovel> {

    private val httpClient = OkHttpClient.Builder()
        .cookieJar(object : CookieJar {
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return listOf(
                    Cookie.Builder().domain("www.esjzone.me").name("ews_key")
                        .value(authorization.ewsKey).build(),
                    Cookie.Builder().domain("www.esjzone.me").name("ews_token")
                        .value(authorization.ewsToken).build()
                )
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            }

        })
        .build()

    private var current: Int = 2
    override fun pages(): Int {
        return this.pages
    }

    override fun more(): List<CoveredNovel> {
        val more = this.more(this.current)
        current += 1
        return more
    }

    override fun more(page: Int): List<CoveredNovel> {
        val response = httpClient.newCall(
            Request.Builder()
                .url("${EsjzoneUrls.Tags}-01/$keyword/$page.html")
                .get()
                .headers(EsjzoneClient.headers)
                .build()
        ).execute()

        val responseBody = response.body!!.string()
        response.close()

        val document = Jsoup.parse(responseBody)

        val novels = mutableListOf<CoveredNovel>()

        for (novelData in EsjzoneXPaths.Tags.Novel.All.evaluate(document).elements) {
            novels.add(
                CoveredNovel(
                    EsjzoneXPaths.Tags.Novel.Cover.evaluate(novelData).get(),
                    EsjzoneXPaths.Tags.Novel.Name.evaluate(novelData).get(),
                    EsjzoneXPaths.Tags.Novel.Url.evaluate(novelData).get(),
                    EsjzoneXPaths.Tags.Novel.Views.evaluate(novelData).get().substring(1).toInt(),
                    EsjzoneXPaths.Tags.Novel.Likes.evaluate(novelData).get().substring(1).toInt(),
                )
            )
        }

        return novels.toList()
    }

    override fun end(): Boolean {
        return current > pages
    }

}