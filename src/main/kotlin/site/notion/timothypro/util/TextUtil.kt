package site.notion.timothypro.util

import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * @author gefangshuai
 * @createDate: 2021/10/17
 */
object TextUtil {
    /**
     * 提取文本中的超链接
     */
    fun getLinks(text: String): MutableList<String> {
        val links = mutableListOf<String>()
        val regex = "https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(text)
        while (m.find()) {
            var urlStr: String = m.group()
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length - 1)
            }
            links.add(urlStr)
        }
        return links
    }
}