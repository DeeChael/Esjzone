package net.deechael.esjzone.network.features

import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import net.deechael.esjzone.network.EsjzoneXPaths
import net.deechael.esjzone.novellibrary.user.UserProfile
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

fun EsjzoneClient.getUserProfile(authorization: Authorization): UserProfile {
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
            .url(EsjzoneUrls.My.Profile)
            .get()
            .headers(this.headers)
            .build()
    ).execute()

    val responseBody = response.body!!.string()
    response.close()

    val document = Jsoup.parse(responseBody)

    return UserProfile(
        EsjzoneXPaths.Profile.Username.evaluate(document).get(),
        EsjzoneXPaths.Profile.AvatarUrl.evaluate(document).get(),
    )
}