package cc.trixey.invero.core.menu

import cc.trixey.invero.core.serialize.ListCommandArgumentSerializer
import cc.trixey.invero.core.serialize.ListStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Invero
 * cc.trixey.invero.core.menu.CommandStructure
 *
 * @author Arasple
 * @since 2023/1/25 19:56
 */
@Serializable
class CommandStructure(
    @SerialName("name")
    val rawName: String,
    @Serializable(with = ListStringSerializer::class)
    val aliases: List<String>?,
    val description: String?,
    val usage: String?,
    val permission: String?,
    val permissionMessage: String?,
    @Serializable(with = ListCommandArgumentSerializer::class)
    val arguments: List<CommandArgument>?
) {

    @Transient
    val name = rawName.lowercase()

}