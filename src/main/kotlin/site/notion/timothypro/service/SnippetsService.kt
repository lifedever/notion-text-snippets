package site.notion.timothypro.service

import cn.hutool.json.JSONArray
import cn.hutool.json.JSONObject
import okhttp3.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import site.notion.timothypro.bean.PageObj

/**
 * @author gefangshuai
 * @createDate: 2021/10/7
 */
@Service
class SnippetsService {
    @Autowired
    private lateinit var notionService: NotionService

    fun save(data: String): Response {
        val blocks = data.split("\n")
        if (blocks.isEmpty()) throw Exception("no data!")
        // 标签
        val tags = JSONArray()
        blocks.findLast { it.contains("#") }?.let { block ->
            block.split("#").lastOrNull()?.trim().let { tag ->
                if (tag == null) {
                    tags.put(mapOf("name" to "未分类"))
                } else {
                    tags.put(mapOf("name" to tag))
                }
            }
        }
        // 页面
        val page = PageObj()
        val properties: MutableMap<String, Any> =
            mutableMapOf(
                "Name" to mapOf(
                    "title" to listOf(
                        mapOf(
                            "type" to "text",
                            "text" to mapOf(
                                "content" to blocks.first()
                            )
                        )
                    )
                )
            )
        if (tags.isNotEmpty()) {
            properties["标签"] = mapOf(
                "multi_select" to tags
            )
        }
        page.properties = properties
        page.children = blocks.filter { it.isNotBlank() }.map { block ->
            mutableMapOf(
                "object" to "block",
                "type" to "paragraph",
                "paragraph" to mapOf(
                    "text" to listOf(
                        mapOf(
                            "type" to "text",
                            "text" to mapOf(
                                "content" to block
                            )
                        )
                    )
                )
            )
        }
        return notionService.createPage(page)
    }
}