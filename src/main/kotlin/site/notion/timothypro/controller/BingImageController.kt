package site.notion.timothypro.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.notion.timothypro.service.BingImageService

@RestController
@RequestMapping("/bing/image")
class BingImageController {
    @Autowired
    private lateinit var bingImageService: BingImageService

    @GetMapping
    fun getImage(): String {
        return bingImageService.getImage()
    }
}