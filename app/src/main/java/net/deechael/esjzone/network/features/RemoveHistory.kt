package net.deechael.esjzone.network.features

import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.AuthorizationCookieJar
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

fun EsjzoneClient.removeHistory(authorization: Authorization, vid: String) {
    val authToken = this.requestAuthToken(authorization, EsjzoneUrls.My.View)

    val httpClient = OkHttpClient.Builder()
        .cookieJar(AuthorizationCookieJar(authorization))
        .build()

    val response = httpClient.newCall(
        Request.Builder()
            .url(EsjzoneUrls.Inc.MemViewDel)
            .post(
                FormBody.Builder()
                    .add("vid", vid)
                    .build()
            )
            .headers(this.headers)
            .header("Authorization", authToken)
            .build()
    ).execute()

    response.close()


}