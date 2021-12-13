package site.notion.timothypro.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author gefangshuai
 * @createDate: 2021/10/9
 */
@RestController
class IndexController {

    @GetMapping("/")
    fun hello(): Map<String, Any> {
        return mapOf(
            "greet" to "Hello World",
            "timestamp" to Date()
        )
    }
}