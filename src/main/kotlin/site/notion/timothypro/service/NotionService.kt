package site.notion.timothypro.service

import cn.hutool.json.JSONObject
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import site.notion.timothypro.bean.PageObj

/**
 * @author gefangshuai
 * @createDate: 2021/10/7
 */
@Service
class NotionService {
    @Value("\${app.notion.notion-version}")
    private lateinit var notionVersion: String
    private var client: OkHttpClient? = null
    private val logger = LoggerFactory.getLogger(NotionService::class.java)

    companion object {
        val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    }

    private fun getClient(): OkHttpClient {
        if (client == null) client = OkHttpClient()
        return client!!
    }

    private fun initPostRequest(url: String, token: String): Request.Builder {
        return Request.Builder()
            .header("Authorization", "Bearer $token")
            .header("Notion-Version", "$notionVersion")
            .header("Content-Type", "application/json")
            .url(url)
    }

    fun createPage(page: PageObj, token: String, parentId: String): Response {
        page.parent = PageObj.Parent(parentId)
        val data = JSONObject(page).toString()
        val request = initPostRequest("https://api.notion.com/v1/pages", token)
            .post(
                data.toRequestBody(JSON)
            ).build()
        logger.info("Post JSON: $data")
        return this.getClient().newCall(request).execute()
    }

}