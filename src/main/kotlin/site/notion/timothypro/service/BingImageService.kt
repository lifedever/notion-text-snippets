package site.notion.timothypro.service

import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONObject
import org.apache.commons.io.IOUtils
import org.apache.tomcat.util.http.fileupload.FileUtils
import org.springframework.stereotype.Service
import java.io.OutputStream
import java.net.URL

@Service
class BingImageService {
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
    private val apiUrl = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN"

    fun getImage(outputStream: OutputStream) {
        val inputStream = URL(getImageUrl()).openStream()
        IOUtils.copy(inputStream, outputStream)
    }

    fun getImageUrl(): String {
        val responseStr = HttpUtil.get(apiUrl)
        return JSONObject(responseStr).getByPath("images[0].url").toString().let {
            "https://cn.bing.com".plus(it)
        }
    }
}