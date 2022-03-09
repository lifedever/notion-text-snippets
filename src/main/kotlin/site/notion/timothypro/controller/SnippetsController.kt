package site.notion.timothypro.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.notion.timothypro.bean.Language
import site.notion.timothypro.context.RequestRecordContext
import site.notion.timothypro.service.SnippetsService
import java.util.*

/**
 * @author gefangshuai
 * @createDate: 2021/10/7
 */
@RequestMapping("/snippets")
@RestController
class SnippetsController {
    @Autowired
    private lateinit var snippetsService: SnippetsService

    @Autowired
    private lateinit var requestRecordContext: RequestRecordContext

    /**
     * @param token
     * @param parentId PageId
     * @param language Language
     */
    @PostMapping
    fun save(
        @RequestHeader token: String,
        @RequestHeader parentId: String,
        @RequestHeader(defaultValue = "zh_CN") language: Language,
        content: String
    ): ResponseEntity<String> {
        val response = snippetsService.save(data = content, token = token, parentId = parentId, language = language)
        requestRecordContext.incCallTimes()
        return ResponseEntity.status(response.code).body(response.body?.string())
    }

    @GetMapping
    fun hello(): Map<String, Any> {
        return mapOf(
            "greet" to "Hello World",
            "timestamp" to Date()
        )
    }
}