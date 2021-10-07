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
        val texts = data.split("#")
        val tags = JSONArray()
        if (texts.size > 1) {
            texts.forEachIndexed { index, txt ->
                if (index > 0) {
                    tags.put(mapOf("name" to txt))
                }
            }
        }
        val page = PageObj()
        val properties: MutableMap<String, Any> =
            mutableMapOf(
                "Name" to mapOf(
                    "title" to listOf(
                        mapOf(
                            "type" to "text",
                            "text" to mapOf(
                                "content" to data
                            )
                        )
                    )
                )
            )
        if (tags.isNotEmpty()) {
            properties["Tags"] = mapOf(
                "multi_select" to tags
            )
        }
        page.properties = properties
        return notionService.createPage(page)
    }
}