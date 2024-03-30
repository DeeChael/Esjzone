package net.deechael.esjzone.network.features

import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.AuthorizationCookieJar
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import net.deechael.esjzone.network.EsjzoneXPaths
import net.deechael.esjzone.network.PageableRequester
import net.deechael.esjzone.novellibrary.novel.CoveredNovel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

fun EsjzoneClient.novels(authorization: Authorization, novelType: Int, sortType: Int): Pair<PageableRequester<CoveredNovel>, List<CoveredNovel>> {
    val httpClient = OkHttpClient.Builder()
        .cookieJar(AuthorizationCookieJar(authorization))
        .build()

    val response = httpClient.newCall(
        Request.Builder()
            .url("${EsjzoneUrls.Base}/list-$novelType$sortType")
            .get()
            .headers(this.headers)
            .build()
    ).execute()

    val responseBody = response.body!!.string()
    response.close()

    val document = Jsoup.parse(responseBody)

    val pages = pagesRegex.find(EsjzoneXPaths.Tags.Pages.evaluate(document).get())!!.groupValues[1].toInt()

    val novels = mutableListOf<CoveredNovel>()

    for (novelData in EsjzoneXPaths.Tags.Novel.All.evaluate(document).elements) {
        novels.add(
            CoveredNovel(
                EsjzoneXPaths.Tags.Novel.Cover.evaluate(novelData).get(),
                EsjzoneXPaths.Tags.Novel.Name.evaluate(novelData).get(),
                EsjzoneXPaths.Tags.Novel.Url.evaluate(novelData).get(),
                EsjzoneXPaths.Tags.Novel.Views.evaluate(novelData).get().substring(1).toInt(),
                EsjzoneXPaths.Tags.Novel.Likes.evaluate(novelData).get().substring(1).toInt(),
                EsjzoneXPaths.Tags.Novel.R18Badge.evaluate(novelData).elements[0].attr("class")
                    .contains("badge")
            )
        )
    }

    return ListNovelRequester(authorization, pages, novelType, sortType) to novels
}


private class ListNovelRequester(
    authorization: Authorization,
    private val pages: Int,
    private val novelType: Int,
    private val sortType: Int
) : PageableRequester<CoveredNovel> {

    private val httpClient = OkHttpClient.Builder()
        .cookieJar(AuthorizationCookieJar(authorization))
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
                .url("${EsjzoneUrls.Base}/list-$novelType$sortType/$page.html")
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
                    EsjzoneXPaths.Tags.Novel.R18Badge.evaluate(novelData).elements[0].attr("class")
                        .contains("badge")
                )
            )
        }

        return novels.toList()
    }

    override fun end(): Boolean {
        return current > pages
    }

}