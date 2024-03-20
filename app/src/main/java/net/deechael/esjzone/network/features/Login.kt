package net.deechael.esjzone.network.features

import com.google.gson.JsonParser
import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.EsjzoneUrls
import net.deechael.esjzone.util.removeLeft
import net.deechael.esjzone.util.removeRight
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

fun EsjzoneClient.login(email: String, password: String): Authorization? {
    var authorization: Authorization? = null
    val httpClient = OkHttpClient.Builder()
        .cookieJar(object : CookieJar {
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return listOf()
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                var ewsKey: String? = null
                var ewsToken: String? = null

                for (cookie in cookies) {
                    if (cookie.name == "ews_key")
                        ewsKey = cookie.value
                    else if (cookie.name == "ews_token")
                        ewsToken = cookie.value
                }

                authorization = Authorization(ewsKey!!, ewsToken!!)
            }

        })
        .build()

    val firstResponse = EMPTY_HTTP_CLIENT.newCall(
        Request.Builder()
            .url(EsjzoneUrls.My.Login)
            .post(
                FormBody.Builder()
                    .add("plxf", "getAuthToken")
                    .build()
            )
            .headers(this.headers)
            .build()
    ).execute()

    val authorizationToken = firstResponse.body!!.string().removeLeft(9).removeRight(10)
    firstResponse.close()

    val secondResponse = httpClient.newCall(
        Request.Builder()
            .url(EsjzoneUrls.Inc.MemLogin)
            .post(
                FormBody.Builder()
                    .add("email", email)
                    .add("pwd", password)
                    .add("remember_me", "on")
                    .build()
            )
            .headers(this.headers)
            .header("Authorization", authorizationToken)
            .build()
    ).execute()

    val data = JsonParser.parseString(secondResponse.body!!.string()).asJsonObject
    secondResponse.close()

    if (data["status"].asInt != 200)
        return null

    return authorization
}
