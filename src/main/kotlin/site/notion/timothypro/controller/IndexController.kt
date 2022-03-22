package site.notion.timothypro.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import site.notion.timothypro.context.RequestRecordContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * @author gefangshuai
 * @createDate: 2021/10/9
 */
@RestController
class IndexController {
    @Autowired
    private lateinit var requestRecordContext: RequestRecordContext

    @GetMapping("/")
    fun hello(): Map<String, Any> {
        return mapOf(
            "Greet" to "Hello Notion Text Snippets!",
            "Timestamp" to LocalDateTime.now(),
            "Call times (Since server up)" to requestRecordContext.getLogs().size,
            "Visitors (Statistics by IP)" to requestRecordContext.getLogs().groupBy { it.ip }.size
        )
    }
}