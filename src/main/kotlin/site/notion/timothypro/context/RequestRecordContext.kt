package site.notion.timothypro.context

import org.springframework.stereotype.Component

/**
 * @author gefangshuai
 * @date 2022/3/9
 */
@Component
class RequestRecordContext {
    var callTimes: Int = 0

    fun incCallTimes() {
        synchronized(this) {
            this.callTimes += 1
        }
    }
}