package site.notion.timothypro.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author gefangshuai
 * @createDate: 2021/10/9
 */
@RestController
class IndexController {

    @GetMapping("/")
    fun index(): String = "hello world!"
}