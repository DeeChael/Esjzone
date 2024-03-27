package net.deechael.esjzone.network.features

import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.AuthorizationCookieJar
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import okhttp3.OkHttpClient
import okhttp3.Request

fun EsjzoneClient.logout(authorization: Authorization) {
    val httpClient = OkHttpClient.Builder()
        .cookieJar(AuthorizationCookieJar(authorization))
        .build()

    httpClient.newCall(
        Request.Builder()
            .url(EsjzoneUrls.My.Logout)
            .get()
            .headers(this.headers)
            .build()
    ).execute().close()
}