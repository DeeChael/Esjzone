package net.deechael.esjzone.network.features

import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import net.deechael.esjzone.network.EsjzoneXPaths
import net.deechael.esjzone.novellibrary.data.HomeData
import net.deechael.esjzone.novellibrary.novel.HomeNovel
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

fun EsjzoneClient.getHomeData(authorization: Authorization): HomeData {
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
            .url(EsjzoneUrls.Home)
            .get()
            .headers(this.headers)
            .build()
    ).execute()

    val responseBody = response.body!!.string()
    response.close()

    val document = Jsoup.parse(responseBody)

    val recentlyUpdateTranslatedNovels = mutableListOf<HomeNovel>()
    val recentlyUpdateOriginalNovels = mutableListOf<HomeNovel>()
    val recentlyUpdateTranslatedR18Novels = mutableListOf<HomeNovel>()
    val recentlyUpdateOriginalR18Novels = mutableListOf<HomeNovel>()
    val recommendationNovels = mutableListOf<HomeNovel>()

    for (recentlyUpdateTranslatedData in EsjzoneXPaths.Home.RecentlyUpdateTranslated.All.evaluate(
        document
    ).elements) {
        recentlyUpdateTranslatedNovels.add(
            HomeNovel(
                EsjzoneXPaths.Home.Novel.Cover.evaluate(recentlyUpdateTranslatedData).get(),
                EsjzoneXPaths.Home.Novel.Name.evaluate(recentlyUpdateTranslatedData).get(),
                EsjzoneXPaths.Home.Novel.Url.evaluate(recentlyUpdateTranslatedData).get(),
                EsjzoneXPaths.Home.Novel.Views.evaluate(recentlyUpdateTranslatedData).get()
                    .substring(1).toInt(),
                EsjzoneXPaths.Home.Novel.Likes.evaluate(recentlyUpdateTranslatedData).get()
                    .substring(1).toInt(),
            )
        )
    }

    for (recentlyUpdateOriginalData in EsjzoneXPaths.Home.RecentlyUpdateOriginal.All.evaluate(
        document
    ).elements) {
        recentlyUpdateOriginalNovels.add(
            HomeNovel(
                EsjzoneXPaths.Home.Novel.Cover.evaluate(recentlyUpdateOriginalData).get(),
                EsjzoneXPaths.Home.Novel.Name.evaluate(recentlyUpdateOriginalData).get(),
                EsjzoneXPaths.Home.Novel.Url.evaluate(recentlyUpdateOriginalData).get(),
                EsjzoneXPaths.Home.Novel.Views.evaluate(recentlyUpdateOriginalData).get()
                    .substring(1).toInt(),
                EsjzoneXPaths.Home.Novel.Likes.evaluate(recentlyUpdateOriginalData).get()
                    .substring(1).toInt(),
            )
        )
    }

    for (recentlyUpdateTranslatedR18Data in EsjzoneXPaths.Home.RecentlyUpdateTranslatedR18.All.evaluate(
        document
    ).elements) {
        recentlyUpdateTranslatedR18Novels.add(
            HomeNovel(
                EsjzoneXPaths.Home.Novel.Cover.evaluate(recentlyUpdateTranslatedR18Data).get(),
                EsjzoneXPaths.Home.Novel.Name.evaluate(recentlyUpdateTranslatedR18Data).get(),
                EsjzoneXPaths.Home.Novel.Url.evaluate(recentlyUpdateTranslatedR18Data).get(),
                EsjzoneXPaths.Home.Novel.Views.evaluate(recentlyUpdateTranslatedR18Data).get()
                    .substring(1).toInt(),
                EsjzoneXPaths.Home.Novel.Likes.evaluate(recentlyUpdateTranslatedR18Data).get()
                    .substring(1).toInt(),
            )
        )
    }

    for (recentlyUpdateOriginalR18Data in EsjzoneXPaths.Home.RecentlyUpdateOriginalR18.All.evaluate(
        document
    ).elements) {
        recentlyUpdateOriginalR18Novels.add(
            HomeNovel(
                EsjzoneXPaths.Home.Novel.Cover.evaluate(recentlyUpdateOriginalR18Data).get(),
                EsjzoneXPaths.Home.Novel.Name.evaluate(recentlyUpdateOriginalR18Data).get(),
                EsjzoneXPaths.Home.Novel.Url.evaluate(recentlyUpdateOriginalR18Data).get(),
                EsjzoneXPaths.Home.Novel.Views.evaluate(recentlyUpdateOriginalR18Data).get()
                    .substring(1).toInt(),
                EsjzoneXPaths.Home.Novel.Likes.evaluate(recentlyUpdateOriginalR18Data).get()
                    .substring(1).toInt(),
            )
        )
    }

    for (recommendationData in EsjzoneXPaths.Home.Recommendation.All.evaluate(document).elements) {
        recommendationNovels.add(
            HomeNovel(
                EsjzoneXPaths.Home.Novel.Cover.evaluate(recommendationData).get(),
                EsjzoneXPaths.Home.Novel.Name.evaluate(recommendationData).get(),
                EsjzoneXPaths.Home.Novel.Url.evaluate(recommendationData).get(),
                EsjzoneXPaths.Home.Novel.Views.evaluate(recommendationData).get().substring(1)
                    .toInt(),
                EsjzoneXPaths.Home.Novel.Likes.evaluate(recommendationData).get().substring(1)
                    .toInt(),
            )
        )
    }

    return HomeData(
        recentlyUpdateTranslatedNovels,
        recentlyUpdateOriginalNovels,
        recentlyUpdateTranslatedR18Novels,
        recentlyUpdateOriginalR18Novels,
        recommendationNovels
    )
}