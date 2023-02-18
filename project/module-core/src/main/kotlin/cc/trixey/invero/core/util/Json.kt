package cc.trixey.invero.core.util

import kotlinx.serialization.json.Json

/**
 * Invero
 * cc.trixey.invero.core.util.Json
 *
 * @author Arasple
 * @since 2023/2/18 13:55
 */
 val prettyJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

 val standardJson = Json