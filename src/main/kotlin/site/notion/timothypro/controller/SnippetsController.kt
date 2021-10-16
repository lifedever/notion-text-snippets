package site.notion.timothypro.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.notion.timothypro.service.SnippetsService

/**
 * @author gefangshuai
 * @createDate: 2021/10/7
 */
@RequestMapping("/snippets")
@RestController
class SnippetsController {
    @Autowired
    private lateinit var snippetsService: SnippetsService

    @PostMapping
    fun save(@RequestHeader token: String, @RequestHeader parentId: String, content: String): ResponseEntity<String> {
        val response = snippetsService.save(data = content, token = token, parentId = content)
        return ResponseEntity.status(response.code).body(response.body?.string())
    }

    @GetMapping
    fun hello() = "Snippets Running!"
}