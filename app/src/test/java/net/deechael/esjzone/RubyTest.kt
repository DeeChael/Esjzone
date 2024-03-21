package net.deechael.esjzone

import net.deechael.esjzone.novellibrary.component.TextComponent
import net.deechael.esjzone.novellibrary.novel.analyseDescription
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.junit.Test

class RubyTest {

    @Test
    fun test() {
        val element = Jsoup.parse("<div><p>呉井雄悟是个随处可见的普通高中生。要说有什么奇怪的地方，就是他特别喜欢周日早上放送的<ruby>英雄<rp>(</rp><rt>Hero</rt><rp>)</rp></ruby>番组。</p><p>那样的他在某一天，为了从随机杀人犯手上保护一对亲子而殒命……结果回过神来，已经作为【<ruby>露米娜丝<rp>(</rp><rt>闪耀</rt><rp>)</rp></ruby>·<ruby>希丝特利<rp>(</rp><rt>纪史</rt><rp>)</rp></ruby>】中、与他同名的登场人物——<ruby>尤格<rp>(</rp><rt>Yugo</rt><rp>)</rp></ruby>·<ruby>克雷<rp>(</rp><rt>Clay</rt><rp>)</rp></ruby>，转生到了异世界。</p><p>尤格是学园里最讨人嫌的恶役，在与玩家——也就是主人公——决斗败北而失去一切后，以直到最后都仰慕着他的弟弟为祭品召唤了魔剑，在与主人公的战斗中丧命的角色。对此一无所知、只是对英雄狂热到像个傻瓜的御宅族，呉井雄悟（十七岁），转生到了这个角色身上，这下可不得了——</p><p>「哇等下~！这不是变身手环吗！呜哇、变身台词，说什么好呢？！」</p><p>「这个铠甲、不是超帅的吗喂！太感谢啦、吾弟哟！」</p><p>毫无自觉、并非有意为之的情况下，不知道这里是游戏世界的尤格，将接连袭来的破灭flag统统粉碎了！</p><p>在<ruby><strong><span style=\"color: rgb(184, 49, 47);\">转生成主人公的其他人<rp>(</rp></span></strong><rt><strong><span style=\"color: rgb(184, 49, 47);\">人渣们</span></strong></rt><rp>)</rp></ruby>因为剧情不按预期发展而困惑时，和最爱的弟弟一起在异世界学园<ruby>猪突猛进<rp>(</rp><rt>勇往直前</rt><rp>)</rp></ruby>的他，究竟会走向怎样的未来呢？！</p><p>「嘛，最终兵器合体<ruby>机甲<rp>(</rp><rt>Robot</rt><rp>)</rp></ruby>造不出来吗？果然不行吗？是吗……那巨大化之类的、啊、那也不行么……唔、说了强人所难的话真是抱歉……」</p><p>……究竟会怎样呢？！</p><p style=\"text-align: center;\">——RX786——</p><p><span style=\"color: rgb(184, 49, 47);\"><strong>机翻&amp;初润：泥岩（RX786）</strong></span></p><p><span style=\"color: rgb(239, 239, 239);\">润色&amp;<strong>鸽子</strong>：白夜（Erovy in Abyss）</span></p><p><span style=\"color: rgb(184, 49, 47);\"><strong>润完的章节会标个☆，请查收~</strong></span></p><p style=\"text-align: center;\">——RX786——</p><p><span style=\"color: rgb(243, 121, 52);\">可以用机翻看原文，但不要留言剧透，一旦发现则将禁止观看，已有一人被禁止观看。</span></p><p><span style=\"color: rgb(243, 121, 52);\">想剧透的都可以进去写讨论帖，想看剧透的也能去看</span></p></div>").testResolve()
        analyseDescription(element)
            .components.forEach {
                if (it is TextComponent) {
                    println(it.getStyles())
                    for (extra in it.getExtras())
                        println(extra.getStyles())
                }
            }
    }


}

private fun Document.testResolve(): Element {
    return this.getElementsByTag("html")[0].getElementsByTag("body")[0].getElementsByTag("div")[0]
}