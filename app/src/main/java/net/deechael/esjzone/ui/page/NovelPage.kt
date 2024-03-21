package net.deechael.esjzone.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import net.deechael.esjzone.novellibrary.component.BackgroundColorTextStyle
import net.deechael.esjzone.novellibrary.component.BoldTextStyle
import net.deechael.esjzone.novellibrary.component.ColorTextStyle
import net.deechael.esjzone.novellibrary.component.FontSizeTextStyle
import net.deechael.esjzone.novellibrary.component.FuriganaTextStyle
import net.deechael.esjzone.novellibrary.component.ImageComponent
import net.deechael.esjzone.novellibrary.novel.NovelDescription
import net.deechael.esjzone.novellibrary.component.TextComponent
import net.deechael.esjzone.novellibrary.novel.analyseDescription
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

@Composable
fun NovelPage() {

}

@Composable
fun Description(description: NovelDescription, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = LocalTextStyle.current
    val density = LocalDensity.current

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            for (component in description.components) {
                if (component is TextComponent) {
                    val (str, inlines) = component.toInlineAnnotatedString(
                        textMeasurer,
                        textStyle,
                        density
                    )
                    Text(
                        text = str,
                        inlineContent = inlines
                    )
                    /*
                                        Text(
                                            text = component.toAnnotatedString(),
                                            modifier = Modifier.padding(3.dp)
                                        )
                                        */
                } else if (component is ImageComponent) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(component.url)
                            .crossfade(true)
                            .build(),
                        contentDescription = "description",
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                end = 8.dp,
                                top = 3.dp,
                                bottom = 3.dp
                            )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FuriganaTestPreview() {
    val description = NovelDescription(
        listOf(
            TextComponent("【").append(
                TextComponent("簡單的概要").style(
                    BoldTextStyle
                )
            ).append(
                TextComponent("】")
            ),
            TextComponent("在慶祝成人的派對上，稍微社交障礙，深藏箱房（物理上）的大小姐莫卡，與第二王子賽方相遇，締下婚約，雖然遇上許多麻煩但最終還是結婚的故事"),
            TextComponent("Color test").style(ColorTextStyle(Color.Blue)),
            TextComponent("Color with bold").style(ColorTextStyle(Color.Blue))
                .style(BoldTextStyle),
            TextComponent("size").style(FontSizeTextStyle(24)),
            TextComponent("background").style(BackgroundColorTextStyle(Color.Cyan)),
            TextComponent("furigana").style(FuriganaTextStyle(TextComponent("test"))),
            TextComponent("furigana with style").style(
                FuriganaTextStyle(
                    TextComponent("test").style(ColorTextStyle(Color.Red))
                        .style(FontSizeTextStyle(24) /* Prevent overflow, size is not allowed in furigana */)
                )
            ),
            TextComponent("furigana with size").style(FontSizeTextStyle(24))
                .style(FuriganaTextStyle(TextComponent("test")))
                .append(TextComponent("append")).append(TextComponent("more"))
                .append(TextComponent("furigana")),
        )
    )

    val textStyle = LocalTextStyle.current
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    Card(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            for (component in description.components) {
                if (component is TextComponent) {
                    val (str, inlines) = component.toInlineAnnotatedString(
                        textMeasurer,
                        textStyle,
                        density
                    )
                    Text(
                        text = str,
                        inlineContent = inlines
                    )
                } else if (component is ImageComponent) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(component.url)
                            .crossfade(true)
                            .build(),
                        contentDescription = "description",
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                end = 8.dp,
                                top = 3.dp,
                                bottom = 3.dp
                            )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DescriptionPreview() {
    Description(
        description = NovelDescription(
            listOf(
                TextComponent("【").append(
                    TextComponent("簡單的概要").style(
                        BoldTextStyle
                    )
                ).append(
                    TextComponent("】")
                ),
                TextComponent("在慶祝成人的派對上，稍微社交障礙，深藏箱房（物理上）的大小姐莫卡，與第二王子賽方相遇，締下婚約，雖然遇上許多麻煩但最終還是結婚的故事"),
                TextComponent("Color test").style(ColorTextStyle(Color.Blue)),
                TextComponent("Color with bold").style(ColorTextStyle(Color.Blue))
                    .style(BoldTextStyle),
                TextComponent("size").style(FontSizeTextStyle(24)),
                TextComponent("background").style(BackgroundColorTextStyle(Color.Cyan)),
            )
        ), // test from: https://www.esjzone.me/detail/1642059588.html
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun DescriptionAnalysisPreview1() {
    val descriptionDivElement = Jsoup.parse(
        "<div><p dir=\"auto\"><span style=\"font-size: 24px;\"><strong>小说web版简介：</strong></span></p><p dir=\"auto\"><strong><span style=\"font-size: 18px;\">我所在的高中里，包括我在内有两个叫佐藤一的人。</span></strong></p><p dir=\"auto\"><span style=\"font-size:       18px;\"><strong>一个是容貌出众、文武双全的篮球部王牌佐藤一。<br>相比之下，我作为“二号”佐藤一，从来就不是那种能成为第一的人，在班里也是不怎么引人注目的存在。 </strong></span></p><p dir=\"auto\"><span style=\"font-size:       18px;\"><strong>就是这样的我，因为一次偶然的机会，开始和班里乃至全年级最受欢迎的南野千夏成为了朋友，我们之间的关系也随之开始慢慢改变。 <br></strong></span></p><p dir=\"auto\"><strong><span style=\"font-size: 18px;\">这是一个关于二号的我与第一的她的故事。</span></strong></p><p dir=\"auto\"><br></p><p><span style=\"font-size:       18px;\"><strong>————后日谭————</strong></span></p><p><strong><span style=\"font-size:       18px;\">这是一部描绘同一所高中里的男女生及其周围人物青春日常的群像剧。</span></strong></p><p><strong><span style=\"font-size:       18px;\">它既展现了一段甜蜜的恋情，也揭示了一场身份差异之恋的挣扎。</span></strong></p><p><strong><span style=\"font-size:       18px;\">既是纯真的单相思，也面临着与往昔恋情的对峙。</span></strong></p><p><strong><span style=\"font-size:       18px;\">时而，它是一个对恋爱尚未了解的故事，时而，又是一个从最恶劣情况中重新开始的旅程。</span></strong></p><p><span style=\"font-size:       18px;\"><strong>多条恋情交错编织，共同构成了这个物语。</strong></span></p><p dir=\"auto\"><br></p><p dir=\"auto\"><span style=\"font-size: 18px;\"><br></span></p><p dir=\"auto\"><span style=\"font-size: 18px;\">※ web版跟文库相差不大，剧情流程和发展都是一样的，文库版稍微丰富了一些对话和描写。另外web已完结，所以不用担心有改变或者调整。</span></p><p dir=\"auto\"><br></p><p dir=\"auto\"><span style=\"font-size: 18px;\">※ 只看标题和简介真的很像是烂大街的桃文。但我要说的是，这是一本非常优秀的恋爱小说！我一开始没什么好印象，看到amazon评论分很高，外加最近书荒才买来消遣的，看完后不得不承认这是一个让人心动的恋爱故事——</span></p><p dir=\"auto\"><span style=\"font-size: 18px;\">小说剧情紧凑，故事铺垫和男女主的刻画的都非常好，整个故事从展开到结束一气呵成不拖泥带水。不是我故意拉高大家的期待，总之欢迎大家为此支付时间来阅读！</span></p><p><br></p><p><span style=\"font-size:       18px;\">※ 第二卷已决定发售！</span></p><p><br></p><p><br></p></div>"
    ).testResolve()
    // test from: https://www.esjzone.me/detail/1704225400.html
    val description = analyseDescription(descriptionDivElement)
    Description(
        description = description,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun DescriptionAnalysisPreview2() {
    val descriptionDivElement =
        Jsoup.parse("<div><p style=\"text-align:center;\"><span style=\"color:rgb(209, 72, 65); font-size:18px;\"><strong>討伐仇敵。在死亡的命運襲來之前。</strong></span></p><p>2023年4月20日，由Fantasia文庫，</p><p>改名為『轉生為註定會被劇情殺「設定上最強角色」的我，決定折斷所有死亡旗幟』書籍化了！！！</p><p><br></p><p>=====</p><p><br></p><p>在邊境的邊陲，緊鄰著魔獸棲息的樹海，與父親兩人過著生活的少年基爾·赫斯特。</p><p>他擁有前世的記憶。</p><p><br></p><p>透過前世的記憶，他知道了。</p><p>他得知自己現在所居住的世界，與前世所遊玩的遊戲『勇幻之刃』所描繪的世界極為相似。</p><p><br></p><p>而他轉生的『基爾·赫斯特』，是第三王女『瑟蕾茵·巴魯提摩亞』的前·護衛，同時也是設定上最強的角色，然而在劇情開始時卻已經身亡了。</p><p><br></p><p>他『基爾·赫斯特』究竟是如何度過一生——遊戲中並未提及這些細節。</p><p><br></p><p>他只是一個在劇情中探討主要女主角瑟蕾茵的過去時才會被提及的存在，至於他有哪些豐功偉業、死於何種原因，以及他的死因是由什麼造成的，這些都不得而知。</p><p><br></p><p>然而，基爾依舊憑藉著設定上最強的潛力，持續向前邁進。</p><p>他結識夥伴、磨練技巧、培養羈絆——追求力量，燃燒生命。</p><p><br></p><p>這一切都是為了對抗即將到來的死亡命運。</p><p><br></p><p><br></p><p>以及，為了向羞辱並殘忍殺害他親生父母的仇人——\"魔神的眷屬\"復仇。</p><p><br></p><p><br></p><p>譯者：用機翻+一點點的潤色，隨緣翻譯，如果有人想接坑請向管理員申請權限<br><strong>還有這是Web版</strong></p></div>")
            .testResolve()
    // test from: https://www.esjzone.me/detail/1709119297.html
    val description = analyseDescription(descriptionDivElement)
    Description(
        description = description,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun DescriptionAnalysisPreview3() {
    val descriptionDivElement = Jsoup.parse(
        "<div><p>呉井雄悟是个随处可见的普通高中生。要说有什么奇怪的地方，就是他特别喜欢周日早上放送的<ruby>英雄<rp>(</rp><rt>Hero</rt><rp>)</rp></ruby>番组。</p><p>那样的他在某一天，为了从随机杀人犯手上保护一对亲子而殒命……结果回过神来，已经作为【<ruby>露米娜丝<rp>(</rp><rt>闪耀</rt><rp>)</rp></ruby>·<ruby>希丝特利<rp>(</rp><rt>纪史</rt><rp>)</rp></ruby>】中、与他同名的登场人物——<ruby>尤格<rp>(</rp><rt>Yugo</rt><rp>)</rp></ruby>·<ruby>克雷<rp>(</rp><rt>Clay</rt><rp>)</rp></ruby>，转生到了异世界。</p><p>尤格是学园里最讨人嫌的恶役，在与玩家——也就是主人公——决斗败北而失去一切后，以直到最后都仰慕着他的弟弟为祭品召唤了魔剑，在与主人公的战斗中丧命的角色。对此一无所知、只是对英雄狂热到像个傻瓜的御宅族，呉井雄悟（十七岁），转生到了这个角色身上，这下可不得了——</p><p>「哇等下~！这不是变身手环吗！呜哇、变身台词，说什么好呢？！」</p><p>「这个铠甲、不是超帅的吗喂！太感谢啦、吾弟哟！」</p><p>毫无自觉、并非有意为之的情况下，不知道这里是游戏世界的尤格，将接连袭来的破灭flag统统粉碎了！</p><p>在<ruby><strong><span style=\"color: rgb(184, 49, 47);\">转生成主人公的其他人<rp>(</rp></span></strong><rt><strong><span style=\"color: rgb(184, 49, 47);\">人渣们</span></strong></rt><rp>)</rp></ruby>因为剧情不按预期发展而困惑时，和最爱的弟弟一起在异世界学园<ruby>猪突猛进<rp>(</rp><rt>勇往直前</rt><rp>)</rp></ruby>的他，究竟会走向怎样的未来呢？！</p><p>「嘛，最终兵器合体<ruby>机甲<rp>(</rp><rt>Robot</rt><rp>)</rp></ruby>造不出来吗？果然不行吗？是吗……那巨大化之类的、啊、那也不行么……唔、说了强人所难的话真是抱歉……」</p><p>……究竟会怎样呢？！</p><p style=\"text-align: center;\">——RX786——</p><p><span style=\"color: rgb(184, 49, 47);\"><strong>机翻&amp;初润：泥岩（RX786）</strong></span></p><p><span style=\"color: rgb(239, 239, 239);\">润色&amp;<strong>鸽子</strong>：白夜（Erovy in Abyss）</span></p><p><span style=\"color: rgb(184, 49, 47);\"><strong>润完的章节会标个☆，请查收~</strong></span></p><p style=\"text-align: center;\">——RX786——</p><p><span style=\"color: rgb(243, 121, 52);\">可以用机翻看原文，但不要留言剧透，一旦发现则将禁止观看，已有一人被禁止观看。</span></p><p><span style=\"color: rgb(243, 121, 52);\">想剧透的都可以进去写讨论帖，想看剧透的也能去看</span></p></div>"
    ).testResolve()
    // test from: https://www.esjzone.me/detail/1701316313.html
    val description = analyseDescription(descriptionDivElement)
    Description(
        description = description,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun DescriptionAnalysisPreview4() {
    val descriptionDivElement =
        Jsoup.parse("<div><p><img src=\"https://telegra.ph/file/27b2ea1cc73b854ab2d02.jpg\" style=\"width: 300px;\" class=\"fr-fic fr-dib\"></p><p><strong style=\"font-size: 18px; -webkit-text-size-adjust: 100%;\">雙刃、奔馳。當扭曲的兩人相遇時，故事便開始了。</strong></p><p><br></p><p><span style=\"-webkit-text-size-adjust: 100%;\">「魔女」───在魔獸和魔術消失的大陸上，唯一剩下的未知存在。向著古老且令人恐懼的存在進行討伐的傭兵齊格，在激戰結束後勉強取得了勝利，但同時也失去了殺死魔女的目的。正當他打算不下致命一擊離去時，魔女向他提出了一個請求。她希望能夠在不被追捕的情況下生活。在掙扎過後，傭兵決定接受這個請求。他意識到，在對魔女充滿敵意的這片大陸上，想要實現這個願望相當困難。因此，齊格決定前往另一個大陸，那裡的存在早已為人所知，而近年來終於有了前往的可能性。</span></p><p><br></p></div>")
            .testResolve()
    // test from: https://www.esjzone.me/detail/1668220987.html
    val description = analyseDescription(descriptionDivElement)
    Description(
        description = description,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun DescriptionAnalysisPreview5() {
    val descriptionDivElement =
        Jsoup.parse("<div><p style=\"text-align:center;\"><img class=\"fr-dib fr-fic\" src=\"https://telegra.ph/file/c77a0868c743fc18e38bc.jpg\" style=\"width:649px;\"></p><p style=\"text-align:center;\"><img class=\"fr-dib fr-fic\" src=\"https://telegra.ph/file/488b6053bf8a2a748743f.jpg\" style=\"width:649px;\"></p><p style=\"text-align:center;\"><img class=\"fr-dib fr-fic\" src=\"https://telegra.ph/file/27f744299f3a5af1e7786.jpg\" style=\"width:645px;\"></p><p style=\"text-align:center;\"><img class=\"fr-dib fr-fic\" src=\"https://telegra.ph/file/04ef660cf4f6df257800d.jpg\" style=\"width:636px;\"></p><p style=\"text-align:center;\"><img class=\"fr-dib fr-fic\" src=\"https://telegra.ph/file/17bfa96cb636a52ca6406.jpg\" style=\"width:633px;\"></p><p><br></p><p style=\"text-align:center;\"><br><span style=\"font-size:18px;\"><strong><span style=\"color:rgb(209, 72, 65);\">如果注意到內容有看不懂或者錯誤的部分，請直接在該話留言回報，謝謝</span></strong></span></p><p><br></p></div>")
            .testResolve()
    // test from: https://www.esjzone.me/detail/1648525787.html
    val description = analyseDescription(descriptionDivElement)
    Description(
        description = description,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun DescriptionAnalysisPreview6() {
    val descriptionDivElement =
        Jsoup.parse("<div><p>常见的异世界转移。</p><p>在被赋予了特殊能力的同级生中，雾岛兰不知为何被赋予了『眷属调教』这种使女孩子奴化的技能。</p><p>因为毫不讲理的理由而被赶出王宫的兰，为了向同班同学复仇和解除自己的欲望（主要这边为主），运用自己被赋予的技能与世界的秩序对抗。</p><p>本剧讲述的是利用出色的技能，让可爱而吝啬的同班同学们成为顺从的奴隶的故事。</p><p><u><strong><span style=\"color: rgb(250, 197, 28); background-color: rgb(226, 80, 65);\">3月16日，我会努力跟上web的进度的（</span></strong></u></p><p><u><strong><span style=\"color: rgb(250, 197, 28); background-color: rgb(226, 80, 65);\">QQ交流群，没事可以加一下：783936137</span></strong></u></p><p><u><strong><span style=\"color: rgb(247, 218, 100); background-color: rgb(84, 172, 210);\">3月17，进度逐渐跟上辣?</span></strong></u></p><p><u><strong><span style=\"color: rgb(226, 80, 65); background-color: rgb(147, 101, 184);\">能不能拜托读者们手下留情，题材不喜欢，内容不爱看可以不看，别给1星评价行不行，谢谢</span></strong></u></p><p><u><strong><span style=\"color: rgb(226, 80, 65); background-color: rgb(247, 218, 100);\">目前前译者的坑已经填完，请期待下次更新（</span></strong></u></p></div>")
            .testResolve()
    // test from: https://www.esjzone.me/detail/1589698607.html
    val description = analyseDescription(descriptionDivElement)
    Description(
        description = description,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun DescriptionAnalysisPreview7() {
    val descriptionDivElement = Jsoup.parse(
        "<div><p><span style=\"font-size:18px;\">出生於貴族家庭的凡，在兩歲時突然想起自己是轉世者。</span><br><br><span style=\"font-size:18px;\">凡表現出了不像幼兒的行為，一度被傳言成神童。</span><br><br><span style=\"font-size:18px;\">然而，在進行魔術適性鑑定儀式時，結果顯示他有制造物品生產系魔法的能力，但卻被譏笑為無用之人，</span><br><span style=\"font-size:18px;\">最終被趕出侯爵家。</span><br><span style=\"font-size:18px;\">在流放到村莊後，凡面臨各種困難，但仍然堅持發展和防禦該村莊，最终成長為一個巨大城市。</span><br>---------------------------------<br>文章是用GPT3.5為主翻譯，加一點潤色為輔。算是測試一下AI。最近開通4.0API了，覺得，句子有比較順。<br>---------------------------------<br>最近很喜歡看大家在下面留言吐糟，雖然作者內文本來很多地方就不合邏輯，所以吐糟的好啊，很好笑啊。<br>--------------------------------<br>潤色的基準，60%都以原文原意下去翻譯，20%會照原文修改句子通順度，10%如果用原文去看跟本看不懂，只能用接近的意思重新編排，5%會補些重點（例如這句話誰講的啊，誰做出了什麼事啊），最後5%就人名或特定名詞之類的。<br>--------------------------------<br>感謝Daytona 、Abay 、TYVM 、siumonmon 、<a href=\"https://www.esjzone.me/my/profile?uid=53938\" target=\"_blank\">星界</a>、<a href=\"https://www.esjzone.me/my/profile?uid=81006\" target=\"_blank\">歐邁尼斯</a>、pwo、yiyuandian 、路人己、<a href=\"https://www.esjzone.me/my/profile?uid=86992\" target=\"_blank\">chondrite</a> 、<a href=\"https://www.esjzone.me/my/profile?uid=151915\" target=\"_blank\">3.14159265358979323846264338</a>、<a href=\"https://www.esjzone.me/my/profile?uid=104980\" target=\"_blank\">largejj</a> 、AIR、BleakBoy，腦洞 、jason38 、流浪者、skr、hjb4081 大大們常常幫小弟回報錯別字。感謝你們的付出。<br>--------------------------------<br>000C-譯者的話（翻譯工具），有我用工具的心得，可以參考看看。<br>-------------------------------<br>如果你是用android系統在看線上小說的話，推薦用靈魂瀏覽器看喔，google市集裡面就有了，它唯一特別的地方，就是它會記錄你看到那裡，你下次在打開時，它會停在你上一次看到的地方。很好用喔。<br>------------------------------<br>除了錯字回報外，如果那裡讀起來卡卡的，也就是你讀起來是看的懂，但是就是卡卡的，也是歡迎用錯字回報的方式回報喔。<br>------------------------------<br>啊，剛發現，如果是主角的父親︰杰爾帕（如果用網站的繁簡轉換或是一些繁簡轉換的程式，會變成傑爾帕......），這，只能請大家自己腦補一下了。<br>-----------------------------<br><br><span style=\"font-size:18px;\">給大家小禮物（真的只是機翻喔）︰<span style=\"color:rgb(44, 130, 201);\"><a href=\"https://books.fishhawk.top/\" target=\"_blank\">機翻網</a></span></span><br><br><span style=\"font-size:18px;\"><span style=\"color:rgb(44, 130, 201);\">--------------------</span></span><br><span style=\"font-size: 24px; color: rgb(209, 72, 65);\">2023 0907</span></p><p><span style=\"font-size: 24px; color: rgb(209, 72, 65);\">終於追上了最新進度了。<br></span><br><span style=\"color:rgb(0, 0, 0); font-size:24px;\">----------------</span><br><span style=\"color:rgb(0, 0, 0); font-size:24px;\"><a href=\"https://klz9.com/ybed-fun-territory-defense-of-the-easy-going-lord-the-nameless-village-is-made-into-the-strongest-fortified-city-by-production-magic.html\" target=\"_blank\"><u>生肉漫畫</u></a> </span><a href=\"https://www.dm5.cn/manhua-anxianlingzhudeyukuailingdifangwei-yishengchanximoshujiangwumingxiaocundazaochengzuiqiangyaosaidushi/\" target=\"_blank\"><span style=\"color:rgb(0, 0, 0); font-size:24px;\"><u>熟肉漫畫</u></span></a><span style=\"color:rgb(0, 0, 0); font-size:24px;\"> <a href=\"https://mangaclash.com/manga/fun-territory-defense-by-the-optimistic-lord/\" target=\"_blank\"><u>另一個生肉</u></a></span></p><p>---------------------------</p><p><span style=\"font-size:       24px;\">另一篇，</span><a href=\"https://www.esjzone.me/detail/1696799207.html\" target=\"_blank\"><span style=\"font-size:       24px;\"><u>便利商店勇者</u></span></a><span style=\"font-size:       24px;\">，正在翻譯中</span></p><p><span style=\"font-size:       24px;\">又不怕死的開了一篇新的，不過那邊算是轉換心情時才會去更新，更新速度可能不會那麼快。<br><a href=\"https://www.esjzone.me/detail/1698166207.html\" target=\"_blank\"><u>追放王子</u></a></span></p><p>----------------------------------------------</p><p><span style=\"font-size: 24px;\">一直都懶的修文，就，有空在說吧。</span></p><p><span style=\"font-size: 24px;\">---------------------------<br>基本上，我很歡迎大家瘋狂吐糟，但是，這只是小說而已，很多東西其實也是作者用私人時間隨便寫寫，讓大家免費翻閱而已，當然有很多地方不好或者是不合理。</span></p><p><span style=\"font-size: 24px;\">重點是，但是，但是，但是（很重要所以說三遍）|<br>如果你的吐糟點很好笑的話，會有對譯者的翻譯速度會產生BUFF的效果。<br>然後，吐糟就別太嚴肅了，好笑的吐糟才是最棒的。</span><br><br></p></div>"
    ).testResolve()
    // test from: https://www.esjzone.me/detail/1679823889.html
    // A funny thing, the render bugs in the website and my code is exactly same! Amazing!
    val description = analyseDescription(descriptionDivElement)
    Description(
        description = description,
        modifier = Modifier.fillMaxSize()
    )
}

private fun Document.testResolve(): Element {
    return this.getElementsByTag("html")[0].getElementsByTag("body")[0].getElementsByTag("div")[0]
}