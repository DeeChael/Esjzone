package net.deechael.esjzone.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class AuthorizationCookieJar(private val authorization: Authorization) : CookieJar {

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return listOf(
            Cookie.Builder().domain(EsjzoneUrls.BaseWithoutProtocol).name("ews_key")
                .value(authorization.ewsKey).build(),
            Cookie.Builder().domain(EsjzoneUrls.BaseWithoutProtocol).name("ews_token")
                .value(authorization.ewsToken).build()
        )
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
    }

}