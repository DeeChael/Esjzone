package net.deechael.esjzone

object Constants {

    val MAINTAINERS = listOf(
        "DeeChael"
    )

    val CONTRIBUTORS = listOf(
        "DeeChael"
    )

    val OPEN_SOURCE_LIBRARIES = listOf(
        GithubRepo(
            "https://github.com/square/okhttp",
            "Square",
            "OkHttp",
            "Square’s meticulous HTTP client for the JVM, Android, and GraalVM."
        ),
        GithubRepo(
            "https://github.com/adrielcafe/voyager",
            "Adriel Café",
            "Voyager",
            "\uD83D\uDEF8 A pragmatic navigation library for Jetpack Compose"
        ),
        GithubRepo(
            "https://github.com/coil-kt/coil",
            "Coil",
            "Coil",
            "Image loading for Android and Compose Multiplatform."
        ),
        GithubRepo(
            "https://github.com/google/gson",
            "Google",
            "Gson",
            "A Java serialization/deserialization library to convert Java Objects into JSON and back"
        ),
        GithubRepo(
            "https://github.com/jhy/jsoup",
            "Jonathan Hedley",
            "jsoup",
            "jsoup: the Java HTML parser, built for HTML editing, cleaning, scraping, and XSS safety."
        ),
        GithubRepo(
            "https://github.com/code4craft/xsoup",
            "Yihua Huang",
            "xsoup",
            "When jsoup meets XPath."
        ),
        GithubRepo(
            "https://github.com/catppuccin/catppuccin",
            "Catppuccin",
            "Catppuccin",
            "\uD83D\uDE38 Soothing pastel theme for the high-spirited!"
        )
    )

}

data class GithubRepo(
    val url: String,
    val owner: String,
    val name: String,
    val description: String
)