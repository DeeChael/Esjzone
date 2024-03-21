package net.deechael.esjzone

import net.deechael.esjzone.novellibrary.novel.NovelDescription
import net.deechael.esjzone.novellibrary.novel.TextDescriptionComponent
import net.deechael.esjzone.novellibrary.novel.analyseDescription
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.junit.Before
import org.junit.Test

class DescriptionTest {

    private lateinit var description: NovelDescription

    @Before
    fun prepare() {
        val descriptionDivElement = Jsoup.parse("<p dir=\"auto\"><span style=\"font-size: 24px;\"><strong>小说web版简介：</strong></span></p><p dir=\"auto\"><strong><span style=\"font-size: 18px;\">我所在的高中里，包括我在内有两个叫佐藤一的人。</span></strong></p><p dir=\"auto\"><span style=\"font-size:       18px;\"><strong>一个是容貌出众、文武双全的篮球部王牌佐藤一。<br>相比之下，我作为“二号”佐藤一，从来就不是那种能成为第一的人，在班里也是不怎么引人注目的存在。 </strong></span></p><p dir=\"auto\"><span style=\"font-size:       18px;\"><strong>就是这样的我，因为一次偶然的机会，开始和班里乃至全年级最受欢迎的南野千夏成为了朋友，我们之间的关系也随之开始慢慢改变。 <br></strong></span></p><p dir=\"auto\"><strong><span style=\"font-size: 18px;\">这是一个关于二号的我与第一的她的故事。</span></strong></p><p dir=\"auto\"><br></p><p><span style=\"font-size:       18px;\"><strong>————后日谭————</strong></span></p><p><strong><span style=\"font-size:       18px;\">这是一部描绘同一所高中里的男女生及其周围人物青春日常的群像剧。</span></strong></p><p><strong><span style=\"font-size:       18px;\">它既展现了一段甜蜜的恋情，也揭示了一场身份差异之恋的挣扎。</span></strong></p><p><strong><span style=\"font-size:       18px;\">既是纯真的单相思，也面临着与往昔恋情的对峙。</span></strong></p><p><strong><span style=\"font-size:       18px;\">时而，它是一个对恋爱尚未了解的故事，时而，又是一个从最恶劣情况中重新开始的旅程。</span></strong></p><p><span style=\"font-size:       18px;\"><strong>多条恋情交错编织，共同构成了这个物语。</strong></span></p><p dir=\"auto\"><br></p><p dir=\"auto\"><span style=\"font-size: 18px;\"><br></span></p><p dir=\"auto\"><span style=\"font-size: 18px;\">※ web版跟文库相差不大，剧情流程和发展都是一样的，文库版稍微丰富了一些对话和描写。另外web已完结，所以不用担心有改变或者调整。</span></p><p dir=\"auto\"><br></p><p dir=\"auto\"><span style=\"font-size: 18px;\">※ 只看标题和简介真的很像是烂大街的桃文。但我要说的是，这是一本非常优秀的恋爱小说！我一开始没什么好印象，看到amazon评论分很高，外加最近书荒才买来消遣的，看完后不得不承认这是一个让人心动的恋爱故事——</span></p><p dir=\"auto\"><span style=\"font-size: 18px;\">小说剧情紧凑，故事铺垫和男女主的刻画的都非常好，整个故事从展开到结束一气呵成不拖泥带水。不是我故意拉高大家的期待，总之欢迎大家为此支付时间来阅读！</span></p><p><br></p><p><span style=\"font-size:       18px;\">※ 第二卷已决定发售！</span></p><p><br></p><p><br></p>", "", Parser.xmlParser())
        // test from: https://www.esjzone.me/detail/1704225400.html

        description = analyseDescription(descriptionDivElement)
        println(descriptionDivElement.childNodes())
    }

    @Test
    fun test() {
        println(description.components)

        for (component in description.components) {
            if (component is TextDescriptionComponent) {
                println(component.javaClass)
            }
        }
    }

}