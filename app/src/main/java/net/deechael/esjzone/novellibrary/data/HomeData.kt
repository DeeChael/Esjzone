package net.deechael.esjzone.novellibrary.data

import net.deechael.esjzone.novellibrary.novel.CoveredNovel

class HomeData(
    val recentlyUpdateTranslated: List<CoveredNovel>,
    val recentlyUpdateOriginal: List<CoveredNovel>,
    val recentlyUpdateTranslatedR18: List<CoveredNovel>,
    val recentlyUpdateOriginalR18: List<CoveredNovel>,
    val recommendation: List<CoveredNovel>
)