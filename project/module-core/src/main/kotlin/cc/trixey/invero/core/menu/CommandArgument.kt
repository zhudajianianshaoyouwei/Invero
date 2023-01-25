package cc.trixey.invero.core.menu

import cc.trixey.invero.core.serialize.CommandArgumentSerailizer
import kotlinx.serialization.Serializable

/**
 * Invero
 * cc.trixey.invero.core.menu.CommandArgument
 *
 * @author Arasple
 * @since 2023/1/25 20:21
 */
@Serializable(with = CommandArgumentSerailizer::class)
class CommandArgument(
    val id: String,
    val type: Type = Type.ANY,
    val suggest: List<String>?,
    val unchecked: Boolean = false,
    val default: Any?,
    val incorrectMessage: String?,
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