package net.deechael.esjzone.novellibrary.data

import net.deechael.esjzone.novellibrary.novel.HomeNovel

class HomeData(
    val recentlyUpdateTranslated: List<HomeNovel>,
    val recentlyUpdateOriginal: List<HomeNovel>,
    val recentlyUpdateTranslatedR18: List<HomeNovel>,
    val recentlyUpdateOriginalR18: List<HomeNovel>,
    val recommendation: List<HomeNovel>
)