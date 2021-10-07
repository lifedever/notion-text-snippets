package site.notion.timothypro.service

import cn.hutool.json.JSONObject
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import site.notion.timothypro.bean.PageObj

/**
 * @author gefangshuai
 * @createDate: 2021/10/7
 */
@Service
class NotionService {
    @Value("\${app.notion.token}")
    private lateinit var token: String

    @Value("\${app.notion.parent-id}")
    private lateinit var parentId: String

    @Value("\${app.notion.notion-version}")
    private lateinit var notionVersion: String
    private var client: OkHttpClient? = null

    companion object {
        val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    }

    private fun getClient(): OkHttpClient {
        if (client == null) client = OkHttpClient()
        return client!!
    }

    private fun initPostRequest(url: String): Request.Builder {
        return Request.Builder()
            .header("Authorization", "Bearer $token")
            .header("Notion-Version", "$notionVersion")
            .header("Content-Type", "application/json")
            .url(url)
    }

    fun createPage(page: PageObj): Response {
        page.parent = PageObj.Parent(parentId)
        val request = initPostRequest("https://api.notion.com/v1/pages")
            .post(
                JSONObject(page).toString().toRequestBody(JSON)
            ).build()
        return this.getClient().newCall(request).execute()
    }

}