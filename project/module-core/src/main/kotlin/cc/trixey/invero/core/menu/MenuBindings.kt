@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.menu

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.chemdah.InferItem.Companion.toInferItem
import cc.trixey.invero.core.BaseMenu
import cc.trixey.invero.core.menu.CommandArgument.Type.*
import cc.trixey.invero.core.serialize.ListStringSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.*
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.common.platform.command.component.CommandComponent
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
    private val item: List<String> = emptyList(),
    @Serializable(with = ListStringSerializer::class) val chat: List<String> = emptyList(),
    @JsonNames("commands", "cmd", "cmds") private val command: JsonElement? = null
) {

    @Transient
    val inferItem = if (!item.isEmpty()) item.toInferItem() else null

    @Transient
    val registeredCommands = mutableSetOf<String>()

    fun register(menu: BaseMenu) {
        Invero.api().getMenuManager().initMenuBindings(menu)

        when (command) {
            is JsonPrimitive -> registerCommandLabel(menu, command)
            is JsonObject -> registerCommandStructure(menu, command)
            is JsonArray -> {
                if (command.firstOrNull() is JsonPrimitive) {
                    command.forEach { registerCommandLabel(menu, it as JsonPrimitive) }
                } else {
                    command.forEach { registerCommandStructure(menu, it as JsonObject) }
                }
            }

            null -> {}
        }
    }

    fun unregister() {
        registeredCommands.forEach { unregisterCommand(it) }
    }

    private fun registerCommandStructure(menu: BaseMenu, jsonObject: JsonObject) {
        Invero.api().getMenuManager().getJsonSerializer<Json>().decodeFromJsonElement<CommandStructure>(jsonObject)
            .apply {
                command(
                    name,
                    aliases ?: emptyList(),
                    description ?: "",
                    usage ?: "",
                    permission ?: "",
                    permissionMessage ?: ""
                ) {
                    // 无参数或没有必选参数，则添加默认执行为打开菜单
                    if (arguments.isNullOrEmpty() || arguments.all { it.optional }) {
                        execute<Player> { sender, _, _ -> menu.open(sender) }
                    }
                    // 标记当前层
                    var layer: CommandComponent = this
                    // 记录截至目前每层的参数
                    val impl = mutableSetOf<String>()
                    // 遍历参数实现
                    arguments?.forEach { argument ->
                        val id = argument.id.also { impl += it }
                        val type = argument.type ?: ANY
                        val default = argument.default
                        val restrict = argument.restrict
                        val suggest = argument.suggest ?: emptyList()

                        layer.dynamic(id, optional) {
                            execute<Player> { sender, ctx, _ ->
                                val variables = buildMap {
                                    impl.forEach { key ->
                                        val value = ctx.getOrNull(key) ?: default ?: error("No valid value")
                                        put(key, value)
                                    }
                                }
                                menu.open(sender, variables)
                            }
                            when (type) {
                                ANY -> suggestion<Player>(!restrict) { _, _ -> suggest }
                                DECIMAL -> restrictDouble()
                                INTEGER -> restrictInt()
                                BOOLEAN -> restrictBoolean()
                                PLAYER -> suggestPlayers(suggest)
                                WORLD -> suggestWorlds(suggest)
                            }
                        }.also { layer = it }
                    }
                }
                registeredCommands += name
                aliases?.map { it.lowercase() }?.let { registeredCommands += it }
            }
    }

    private fun registerCommandLabel(menu: BaseMenu, jsonPrimitive: JsonPrimitive) {
        val id = menu.id ?: return
        val content = jsonPrimitive.contentOrNull?.lowercase() ?: return
        command(content) { execute<Player> { sender, _, _ -> Invero.api().getMenuManager().getMenu(id)?.open(sender) } }
        registeredCommands += content
    }

}