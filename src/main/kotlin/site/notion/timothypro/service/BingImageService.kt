package site.notion.timothypro.service

import cn.hutool.core.io.FileUtil
import cn.hutool.http.HttpRequest
import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONObject
import org.apache.commons.io.IOUtils
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import site.notion.timothypro.util.pathAppend
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

@Service
class BingImageService {
    private val logger = LoggerFactory.getLogger(BingImageService::class.java)

    @Value("\${app.wallpaper.storage-path}")
    private lateinit var wrapperStoragePath: String

    /**

    参数名称	值含义
    format（非必需）	返回数据格式，不存在返回xml格式
    js (返回json格式，一般使用这个)
    xml（返回xml格式）
    idx (非必需)	请求图片截止天数
    0 今天
    -1 截止至明天（预准备的）
    1 截止至昨天，类推（目前最多获取到16天前的图片）
    n（必需）	1-8 返回请求数量，目前最多一次获取8张
    mkt（非必需）	地区
    zh-CN
    ...
     */

    fun getImage(outputStream: OutputStream, mkt: String) {
        val destPath = wrapperStoragePath.pathAppend(mkt).pathAppend(DateTime.now().toString("yyyy-MM-dd").plus(".jpg"))
        FileUtil.mkParentDirs(destPath)
        val imageFile = File(destPath)
        if (!imageFile.exists())
            HttpUtil.downloadFile(getImageUrl(mkt), imageFile)

        val inputStream = FileInputStream(imageFile)
        IOUtils.copy(inputStream, outputStream)
        IOUtils.close(inputStream)
    }

    fun getImageUrl(mkt: String): String {
        val apiUrl = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=${mkt}"
        val bingURL = "https://www.bing.com"
        val responseStr = HttpRequest.get(apiUrl)
            .header("Referer", bingURL)
            .header(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/8.0; .NET4.0C; .NET4.0E)"
            ).execute()
            .body()
        logger.info("responseStr: {}", responseStr)
        return JSONObject(responseStr).getByPath("images[0].url").toString().let {
            bingURL.pathAppend(it)
        }
    }
}