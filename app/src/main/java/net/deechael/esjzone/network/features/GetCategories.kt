package net.deechael.esjzone.network.features

import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.AuthorizationCookieJar
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import net.deechael.esjzone.network.EsjzoneXPaths
import net.deechael.esjzone.novellibrary.novel.Category
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

fun EsjzoneClient.getCategories(authorization: Authorization): List<Category> {
    val httpClient = OkHttpClient.Builder()
        .cookieJar(AuthorizationCookieJar(authorization))
        .build()

    val response = httpClient.newCall(
        Request.Builder()
            .url(EsjzoneUrls.Forum)
            .get()
            .headers(this.headers)
            .build()
    ).execute()

    val responseBody = response.body!!.string()
    response.close()

    val document = Jsoup.parse(responseBody)

    val categories = mutableListOf<Category>()

    for (element in EsjzoneXPaths.Forum.Category.evaluate(document).elements) {
        val name = element.text()
        categories.add(
            Category(
                name,
                element.attr("href"),
                name.contains("r18", ignoreCase = true)
            )
        )
    }

    println("Categories: $categories")

    return categories.toList()
}