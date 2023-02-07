package cc.trixey.invero.core.script.js

import java.net.URL

/**
 * Invero
 * cc.trixey.invero.core.script.JavaScriptUtils
 *
 * @author Arasple
 * @since 2023/2/5 11:51
 */
class Helper {

    fun fromURL(url: String): String {
        return try {
            String(URL(url).openStream().readBytes())
        } catch (t: Throwable) {
            "<ERROR: ${t.localizedMessage}>"
        }
    }

}