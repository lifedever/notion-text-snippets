package site.notion.timothypro

import org.junit.jupiter.api.Test
import site.notion.timothypro.util.TextUtil
import kotlin.math.log

//@SpringBootTest
class NotionWechatSnippetsApplicationTests {

    @Test
    fun pullLinks() {
        val str =
            "我公司的网址是https://www.manyibar.com，但是我的博客网址是http://corebook.notion.site这个，而我这篇文章的地址是：https://corebook.notion.site/C-9f13478da6fc4974a86a72f1cd6f337a"
        val links = TextUtil.getLinks(str)
        val regex = "https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]"
        val seqs = str.splitToSequence(*links.toTypedArray())
        seqs.forEach {
            println(it)
        }
    }

}
