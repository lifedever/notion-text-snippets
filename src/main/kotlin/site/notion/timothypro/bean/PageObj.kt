package site.notion.timothypro.bean

import cn.hutool.json.JSONObject

/**
 * @author gefangshuai
 * @createDate: 2021/10/7
 */
data class PageObj(
    var parent: Parent? = null,
    var properties: MutableMap<String, Any>? = null
) {
    data class Parent(
        val database_id: String
    )
}