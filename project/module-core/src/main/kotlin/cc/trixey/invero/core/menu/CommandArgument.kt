@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.menu

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import kotlinx.serialization.json.JsonPrimitive

/**
 * Invero
 * cc.trixey.invero.core.menu.CommandArgument
 *
 * @author Arasple
 * @since 2023/1/25 20:21
 */
@Serializable
class CommandArgument(
    @JsonNames("key", "name", "label")
    val id: String,
    val type: Type?,
    val suggest: List<String>?,
    val restrict: Boolean = false,
    @JsonNames("optional")
    val optional: Boolean = true,
    val default: JsonPrimitive?,
    val incorrectMessage: String?
) {

    enum class Type {

        ANY,

        DECIMAL,

        INTEGER,

        BOOLEAN,

        PLAYER,

        WORLD

    }

}