package cc.trixey.invero.common.util

/**
 * Invero
 * cc.trixey.invero.common.util.Paste
 *
 * @author Arasple
 * @since 2023/2/13 22:18
 */
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.paste.gg/v1"
private val isoInstant = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

fun createPaste(
    name: String,
    value: String,
    highlightLanguage: String? = null,
    block: PasteContent.() -> Unit = {}
): PasteContent {
    return PasteContent(name, value, highlightLanguage).also(block)
}

fun paste(
    name: String,
    description: String,
    visibility: PasteVisibility = PasteVisibility.UNLISTED,
    last: Long = -1,
    unit: TimeUnit = TimeUnit.MINUTES,
    vararg content: PasteContent
): PasteResult {
    val jsonInput = buildJsonObject {
        put("name", name)
        put("description", description)
        put("visibility", visibility.tag)
        if (last > 0) {
            val date = Date((System.currentTimeMillis() + unit.toMillis(last)))
            put("expires", isoInstant.format(date))
        }
        put("files", buildJsonArray {
            content.forEach {
                add(
                    buildJsonObject {
                        put("name", it.name)
                        put("content", buildJsonObject {
                            put("format", "text")
                            put("highlight_language", it.highlightLanguage)
                            put("value", it.value)
                        })
                    }
                )
            }
        })
    }.let { Json.encodeToString(it) }

    val url = URL("$BASE_URL/pastes")
    val conn = url.openConnection() as HttpURLConnection
    conn.requestMethod = "POST"
    conn.doOutput = true
    conn.setRequestProperty("Content-Type", "application/json")
    conn.outputStream.write(jsonInput.toByteArray())
    val response = conn.inputStream.reader().readLines().joinToString("\n")

    Json.decodeFromString<JsonObject>(response).apply {
        val status = PasteResult.Status.valueOf(this["status"]!!.jsonPrimitive.content.uppercase())
        val result = this["result"]?.jsonObject

        return PasteResult(status, result)
    }
}

enum class PasteVisibility(val tag: String) {

    PUBLIC("public"),

    UNLISTED("*unlisted"),

    PRIVATE("private")

}

class PasteResult(val status: Status, val result: JsonObject?) {

    enum class Status {

        SUCCESS,

        ERROR

    }

    val anonymousLink: String
        get() = "https://paste.gg/p/anonymous/${result?.get("id")}"

}

class PasteContent internal constructor(val name: String, val value: String, val highlightLanguage: String? = null)