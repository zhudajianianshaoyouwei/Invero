package cc.trixey.invero.core.geneartor

import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.serialize.IconSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Invero
 * cc.trixey.invero.core.geneartor.GeneratorSettings
 *
 * @author Arasple
 * @since 2023/1/29 22:23
 */
@Serializable
class GeneratorSettings(
    @SerialName("source")
    val source: String?,
    val elements: List<JsonObject>?,
    val filter: String?,
    val sortBy: String?,
    @SerialName("extension")
    val extenedProperties: Map<String, String>?,
    @Serializable(with = IconSerializer::class)
    val output: Icon,
) {

    @Transient
    val extenedObjects = elements?.map { jsonObject ->
        jsonObject
            .map { it.key to it.value.jsonPrimitive.content }
            .toMap()
            .let { Object(it) }
    }

    fun create() = GeneratorManager.generateSourceBy(source ?: "custom")

}