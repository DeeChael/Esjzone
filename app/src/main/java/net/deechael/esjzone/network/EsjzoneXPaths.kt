package net.deechael.esjzone.network

import us.codecraft.xsoup.XPathEvaluator
import us.codecraft.xsoup.Xsoup

object EsjzoneXPaths {

    object Home {

        object RecentlyUpdateTranslated {

            val All: XPathEvaluator =
                Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[3]/div")

        }

        object RecentlyUpdateOriginal {

            val All: XPathEvaluator =
                Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[5]/div")

        }

        object RecentlyUpdateTranslatedR18 {

            val All: XPathEvaluator =
                Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[7]/div")

        }

        object RecentlyUpdateOriginalR18 {

            val All: XPathEvaluator =
                Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[9]/div")

        }

        object Recommendation {

            val All: XPathEvaluator =
                Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[11]/div")

        }

        object Novel {

            val Cover: XPathEvaluator =
                Xsoup.compile("/div/a/div/div/div/@data-src") // lazy-load data, in real browser this will be removed by auto-loaded js
            val Name: XPathEvaluator = Xsoup.compile("/div/div/h5/a/text()")
            val Url: XPathEvaluator =
                Xsoup.compile("/div/div/h5/a/@href") // example: /detail/1689480747.html
            val Views: XPathEvaluator =
                Xsoup.compile("/div/div/div[2]/div[1]/text()") // example: " 1234", so need remove the spaces and parse it to int
            val Likes: XPathEvaluator =
                Xsoup.compile("/div/div/div[2]/div[2]/text()") // example: " 1234", so need remove the spaces and parse it to int

        }

    }

    object Profile {

        val Username: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/aside/form/div[2]/h4/text()")
        val AvatarUrl: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/aside/form/div[1]/img/@src")

    }

}