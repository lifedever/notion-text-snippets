package site.notion.timothypro.service

import cn.hutool.json.JSONArray
import okhttp3.Response
import org.apache.commons.validator.routines.UrlValidator
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import site.notion.timothypro.bean.Language
import site.notion.timothypro.bean.PageObj
import site.notion.timothypro.util.TextUtil


/**
 * @author gefangshuai
 * @createDate: 2021/10/7
 */
@Service
class SnippetsService {
    @Autowired
    private lateinit var notionService: NotionService

    private fun getTagKey(language: Language): String {
        return when(language) {
            Language.zh_CN -> "标签"
            Language.en_US -> "Tags"
        }
    }

    private fun getDefaultTagName(language: Language): String {
        return when(language) {
            Language.zh_CN -> "未分类"
            Language.en_US -> "Unsorted"
        }
    }

    fun save(data: String, token: String, parentId: String, language: Language): Response {
        val blocks = data.split("\n")
        if (blocks.isEmpty()) throw Exception("no data!")
        // 标签
        val tags = JSONArray()
        blocks.findLast { it.contains("#") }?.let { block ->
            block.split("#").lastOrNull()?.trim()?.let { tag ->
                tags.put(mapOf("name" to tag))
            }
        } ?: let {
            tags.put(mapOf("name" to this.getDefaultTagName(language)))
        }
        // 页面
        val page = if (blocks.size == 1) {
            val url = if (blocks.first().contains("#") && blocks.first().split("#").size == 2) {
                blocks.first().split("#").first()
            } else {
                blocks.first()
            }.trim()
            if (UrlValidator.getInstance().isValid(url)) {
                this.parseUrl(url, tags, language)
            } else {
                this.parseNormal(blocks, tags, language)
            }
        } else {
            this.parseNormal(blocks, tags, language)
        }
        return notionService.createPage(page = page, token = token, parentId = parentId)
    }

    /**
     * 解析url
     */
    private fun parseUrl(url: String, tags: JSONArray, language: Language): PageObj {
        val doc: Document = Jsoup.connect(url).get()
        val title = doc.title()
        val children = listOf(
            mutableMapOf(
                "object" to "block",
                "type" to "paragraph",
                "paragraph" to mapOf(
                    "text" to listOf(
                        mapOf(
                            "type" to "text",
                            "text" to mapOf(
                                "content" to url,
                                "link" to mapOf(
                                    "url" to url
                                )
                            )
                        )
                    )
                )
            )
        )
        val page = PageObj()
        page.properties = this.getProperties(title, tags, language)
        page.children = children
        return page
    }

    /**
     * 普通解析
     */
    private fun parseNormal(blocks: List<String>, tags: JSONArray, language: Language): PageObj {
        val page = PageObj()

        page.properties = this.getProperties(blocks.first().trim(), tags, language)
        page.children = this.getChildren(blocks)
        return page
    }

    /**
     * 获取属性
     */
    private fun getProperties(title: String, tags: JSONArray, language: Language): MutableMap<String, Any> {
        val properties: MutableMap<String, Any> =
            mutableMapOf(
                "Name" to mapOf(
                    "title" to listOf(
                        mapOf(
                            "type" to "text",
                            "text" to mapOf(
                                "content" to title
                            )
                        )
                    )
                )
            )
        if (tags.isNotEmpty()) {
            properties[this.getTagKey(language)] = mapOf(
                "multi_select" to tags
            )
        }
        return properties
    }

    /**
     * 获取文章内容
     */
    private fun getChildren(blocks: List<String>): List<MutableMap<String, Any>> {
        return blocks.filter { it.isNotBlank() }.map { block ->
            mutableMapOf(
                "object" to "block",
                "type" to "paragraph",
                "paragraph" to mapOf(
                    "text" to this.resolveBlock(block)
                )
            )
        }
    }

    /**
     * 解析一行数据
     *  - url 解析
     */
    private fun resolveBlock(block: String): List<Map<String, *>> {
        val links = TextUtil.getLinks(block)
        return if (links.isEmpty()) {
            listOf(
                mapOf(
                    "type" to "text",
                    "text" to mapOf(
                        "content" to block
                    )
                )
            )
        } else {
            val texts = block.splitToSequence(*links.toTypedArray())
            val results = mutableListOf<Map<String, *>>()
            texts.forEachIndexed { index, txt ->
                results.add(
                    mapOf(
                        "type" to "text",
                        "text" to mapOf(
                            "content" to txt
                        )
                    )
                )
                if (index < links.size) {
                    results.add(
                        mapOf(
                            "type" to "text",
                            "text" to mapOf(
                                "content" to links[index],
                                "link" to mapOf(
                                    "url" to links[index]
                                )
                            )
                        )
                    )
                }
            }

            return results
        }
    }
}