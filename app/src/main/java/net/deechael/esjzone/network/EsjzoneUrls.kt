package net.deechael.esjzone.network

object EsjzoneUrls {

    const val BaseWithoutProtocol: String = "www.esjzone.me"
    const val Base: String = "https://$BaseWithoutProtocol"

    const val EmptyCover: String = "$Base/assets/img/empty.jpg"
    const val Home: String = Base
    const val Tags: String = "$Base/tags"

    object My {

        const val Profile: String = "$Base/my/profile"

        const val Login: String = "$Base/my/login"

        const val Favorite: String = "$Base/my/favorite"

        const val View: String = "$Base/my/view"

    }

    object Inc {

        const val MemLogin: String = "$Base/inc/mem_login.php"

        const val MemFavorite: String = "$Base/inc/mem_favorite.php"

    }

    object Novel {

        const val AllRecentlyUpdate: String = "$Base/list-01"
        const val AllRecentlyUpload: String = "$Base/list-02"
        const val AllHighestRating: String = "$Base/list-03"
        const val AllMostViews: String = "$Base/list-04"
        const val AllMostChapters: String = "$Base/list-05"
        const val AllMostComments: String = "$Base/list-06"
        const val AllMostFavorites: String = "$Base/list-07"
        const val AllMostWords: String = "$Base/list-08"

        const val JapaneseRecentlyUpload: String = "$Base/list-12"
        const val JapaneseRecentlyUpdate: String = "$Base/list-11"
        const val JapaneseHighestRating: String = "$Base/list-13"
        const val JapaneseMostViews: String = "$Base/list-14"
        const val JapaneseMostChapters: String = "$Base/list-15"
        const val JapaneseMostComments: String = "$Base/list-16"
        const val JapaneseMostFavorites: String = "$Base/list-17"
        const val JapaneseMostWords: String = "$Base/list-18"

        const val OriginalRecentlyUpdate: String = "$Base/list-21"
        const val OriginalRecentlyUpload: String = "$Base/list-22"
        const val OriginalHighestRating: String = "$Base/list-23"
        const val OriginalMostViews: String = "$Base/list-24"
        const val OriginalMostChapters: String = "$Base/list-25"
        const val OriginalMostComments: String = "$Base/list-26"
        const val OriginalMostFavorites: String = "$Base/list-27"
        const val OriginalMostWords: String = "$Base/list-28"

        const val KoreanRecentlyUpload: String = "$Base/list-32"
        const val KoreanRecentlyUpdate: String = "$Base/list-31"
        const val KoreanHighestRating: String = "$Base/list-33"
        const val KoreanMostViews: String = "$Base/list-34"
        const val KoreanMostChapters: String = "$Base/list-35"
        const val KoreanMostComments: String = "$Base/list-36"
        const val KoreanMostFavorites: String = "$Base/list-37"
        const val KoreanMostWords: String = "$Base/list-38"

    }

}