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

            val R18Badge: XPathEvaluator = Xsoup.compile("/div/div[1]")

        }

    }

    object Detail {

        val Cover: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[1]/div[1]/div[1]/a/img/@src") // attention: not all the novels have cover
        val Views: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[1]/div[2]/span/label[1]/span/text()")
        val Likes: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[1]/div[2]/span/label[2]/span/text()")
        val Words: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[1]/div[2]/span/label[3]/span/text()")
        val Type: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[1]/div[2]/ul/li[1]/text()")
        val Author: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[1]/div[2]/ul/li[2]/a/text()")
        val ForumUrl: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[1]/div[2]/div[2]/div/a[1]/@href")
        val Tags: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[2]/section/a/text()")
        val Description: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[2]/div/div/div") // used with NovelDescription#analyseDescription
        val ChapterList: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[5]/div/div/div/div[2]")

        val FavoriteText: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[1]/div[2]/div[2]/div/button[1]/span/text()") // 已收藏/收藏

        object Comment {

            val Pages: XPathEvaluator =
                Xsoup.compile("/html/body/div[3]/section/div/div[1]/section")


        }

        object ChapterListDetails {

            val Title: XPathEvaluator = Xsoup.compile("/summary")

        }

    }

    object Tags {

        val Pages: XPathEvaluator = Xsoup.compile("/html/body/script[20]")

        object Novel {

            val All: XPathEvaluator =
                Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[3]/div")

            val Cover: XPathEvaluator = Xsoup.compile("/div/a/div/div/div/@data-src")
            val Name: XPathEvaluator = Xsoup.compile("/div/div/h5/a/text()")
            val Url: XPathEvaluator = Xsoup.compile("/div/div/h5/a/@href")
            val Views: XPathEvaluator = Xsoup.compile("/div/div/div[4]/div[1]/text()")
            val Likes: XPathEvaluator = Xsoup.compile("/div/div/div[4]/div[2]/text()")

            val R18Badge: XPathEvaluator = Xsoup.compile("/div/div[1]")

        }

    }

    object Forum {

        val Category: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[1]/table/tbody/tr/td/a")
        val Novel: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div/div/table/tbody/tr/td/a") // text() for novel name, @href for link
        val Content: XPathEvaluator = Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[3]")

        val PreviousChapter: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[2]/div[1]/a")
        val NextChapter: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/div[2]/div[3]/a")

    }

    object Profile {

        val Username: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/aside/form/div[2]/h4/text()")
        val AvatarUrl: XPathEvaluator =
            Xsoup.compile("/html/body/div[3]/section/div/div[1]/aside/form/div[1]/img/@src")

        object Favorite {

            val Pages: XPathEvaluator = Xsoup.compile("/html/body/script[21]")

            val Novel: XPathEvaluator =
                Xsoup.compile("/html/body/div[3]/section/div/div[2]/div[3]/table/tbody/tr/td/div/div/h5/a")

        }

        object View {

            val Novel: XPathEvaluator =
                Xsoup.compile("/html/body/div[3]/section/div/div[2]/div[2]/table/tbody/tr")
            val TitleAndUrl: XPathEvaluator = Xsoup.compile("/td/div/div/div[1]/div[1]/h5/a")
            val Chapter: XPathEvaluator = Xsoup.compile("/td/div/div/div[2]/span/a")

        }

    }

}