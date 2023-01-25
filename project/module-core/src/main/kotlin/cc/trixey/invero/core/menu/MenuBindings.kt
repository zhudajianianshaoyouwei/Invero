@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.menu

import cc.trixey.invero.core.InveroManager
import cc.trixey.invero.core.Menu
import cc.trixey.invero.core.menu.CommandArgument.Type.*
import cc.trixey.invero.core.serialize.ListStringSerializer
import cc.trixey.invero.library.chemdah.InferItem.Companion.toInferItem
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.*
import org.bukkit.entity.Player
import taboolib.common.platform.command.command
import taboolib.common.platform.command.component.CommandComponent
import taboolib.common.platform.command.restrictBoolean
import taboolib.common.platform.command.restrictDouble
import taboolib.common.platform.command.restrictInt
import taboolib.common.platform.function.unregisterCommand

/**
 * Invero
 * cc.trixey.invero.core.menu.MenuBindings
 *
 * @author Arasple
 * @since 2023/1/25 11:30
 */
@Serializable
class MenuBindings(
    @Serializable(with = ListStringSerializer::class)
    @JsonNames("items")
    private val item: List<String> = listOf(),
    @Serializable(with = ListStringSerializer::class)
    private val chat: List<String> = listOf(),
    @JsonNames("items")
    private val command: JsonElement? = null
) {

    @Transient
    val inferItem = item.toInferItem()

    @Transient
    val inferChat = chat.map { it.toRegex() }

    @Transient
    val registeredCommands = mutableSetOf<String>()

    fun register(menu: Menu) {
        val id = menu.name!!
        InveroManager.bindings[id] = this

        when (command) {
            is JsonPrimitive -> registerCommandLabel(menu, command)
            is JsonObject -> registerCommandStrucutre(menu, command)
            is JsonArray -> {
                if (command.firstOrNull() is JsonPrimitive) {
                    command.forEach { registerCommandLabel(menu, it as JsonPrimitive) }
                } else {
                    command.forEach { registerCommandStrucutre(menu, it as JsonObject) }
                }
            }

            null -> {}
        }
    }

    fun unregister() {
        registeredCommands.forEach { unregisterCommand(it) }
    }

    private fun registerCommandStrucutre(menu: Menu, jsonObject: JsonObject) {
        Json
            .decodeFromJsonElement<CommandStructure>(jsonObject)
            .apply {
                command(
                    name,
                    aliases ?: emptyList(),
                    description ?: "",
                    usage ?: "",
                    permission ?: "",
                    permissionMessage ?: ""
                ) {
                    execute<Player> { sender, _, _ -> menu.open(sender) }
                    var layer: CommandComponent = this
                    val implmeneted = mutableSetOf<String>()

                    arguments?.forEach { argument ->
                        val id = argument.id
                        val type = argument.type
                        val default = argument.default
                        val optional = default != null

                        implmeneted += id

                        layer.dynamic(id, optional) {
                            execute<Player> { sender, ctx, _ ->
                                val variables = buildMap {
                                    implmeneted.forEach { key ->
                                        val value = ctx.getOrNull(key) ?: default ?: error("No valid value")
                                        put("_args_$key", value)
                                    }
                                }
                                menu.open(sender, variables)
                            }
                            when (type) {
                                DECIMAL -> restrictDouble()
                                INTEGER -> restrictInt()
                                BOOLEAN -> restrictBoolean()
                                else -> {}
                            }
                        }.also { layer = it }
                    }
                }
                registeredCommands += name
                aliases?.map { it.lowercase() }?.let { registeredCommands += it }
            }
    }

    private fun registerCommandLabel(menu: Menu, jsonPrimitive: JsonPrimitive) {
        val id = menu.name ?: return
        val content = jsonPrimitive.contentOrNull?.lowercase() ?: return
        command(content) { execute<Player> { sender, _, _ -> InveroManager.getMenu(id)?.open(sender) } }
        registeredCommands += content
    }

}