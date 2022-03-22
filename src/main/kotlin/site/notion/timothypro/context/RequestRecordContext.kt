package site.notion.timothypro.context

import cn.hutool.extra.servlet.ServletUtil
import org.springframework.stereotype.Component
import site.notion.timothypro.bean.AccessLogItem
import java.util.Date
import javax.servlet.http.HttpServletRequest

/**
 * @author gefangshuai
 * @date 2022/3/9
 */
@Component
class RequestRecordContext {
    private var logs: MutableList<AccessLogItem> = mutableListOf()

    fun log(request: HttpServletRequest) {
        logs.add(
            AccessLogItem(
                ip = ServletUtil.getClientIP(request),
                accessTime = Date()
            )
        )
    }

    fun getLogs() = logs

}