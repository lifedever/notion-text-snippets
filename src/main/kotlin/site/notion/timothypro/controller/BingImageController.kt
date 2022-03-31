package site.notion.timothypro.controller

import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import site.notion.timothypro.service.BingImageService
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/bing/image")
class BingImageController {
    @Autowired
    private lateinit var bingImageService: BingImageService

    @GetMapping
    fun getImage(response: HttpServletResponse, @RequestParam(defaultValue = "zh-CN") mkt: String) {
        try {
            response.setHeader("Content-Type", "image/png")
            response.contentType = "image/png"
            response.setDateHeader("expires", DateTime.now().plusHours(6).millis)
            response.setHeader("Cache-Control", "Public")
            response.setHeader("Pragma", "Public")
            bingImageService.getImage(response.outputStream, mkt)
            response.flushBuffer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}