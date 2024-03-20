package net.deechael.esjzone.network

import okhttp3.Headers
import okhttp3.OkHttpClient

object EsjzoneClient {

    /* Login steps:
     * 1. POST: Url: https://www.esjzone.me/my/login
     *          Payload: { "plxf": "getAuthToken" }
     *          Response: <JinJing>{login_token}</JinJing>
     *
     * 2. POST: Url: https://www.esjzone.me/inc/mem_login.php
     *          Headers: { "Authorization": "{login_token}" }
     *          Payload: { "email": "{email}", "pwd": "{password}", "remember_me": "on" }
     *          Response: !None!
     *          Set-Cookie: { "ews_key": "{ews_key}", "ews_token": "{ews_token}" }
     *
     * 3. Request everything with cookies {ews_key} and {ews_token} for authorization!
     *
     *
     * Logout steps:
     * GET: https://www.esjzone.me/my/logout
     * with cookies {ews_key} and {ews_token}
     *
     * After this, {ews_key} and {ews_token} will expire
     *
     */

    val headers = Headers.Builder()
        .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
        .build()

    var EMPTY_HTTP_CLIENT = OkHttpClient()

}