package cc.trixey.invero.plugin.dev

import cc.trixey.invero.core.Menu
import cc.trixey.invero.core.serialize.hocon.HoconLoader
import cc.trixey.invero.core.serialize.toJson
import cc.trixey.invero.core.serialize.toMenu
import cc.trixey.invero.core.util.debug
import cc.trixey.invero.core.util.listRecursively
import cc.trixey.invero.core.util.printCatching
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.module.configuration.Type
import taboolib.platform.util.bukkitPlugin
import java.io.File

/**
 * Invero
 * cc.trixey.invero.plugin.dev.InveroDev
 *
 * @author Arasple
 * @since 2023/1/15 15:44
 */
@CommandHeader(name = "invero")
object InveroDev {

    val menus = mutableListOf<Menu>()

    @Awake(LifeCycle.ACTIVE)
    fun invoke() = reload()

    @CommandBody
    val reload = subCommand {
        execute<CommandSender> { _, _, _ -> reload() }
    }

    @CommandBody
    val open = subCommand {
        execute<Player> { player, _, argument ->
            val menuId = argument.split(" ").getOrNull(1)
            menus
                .find { it.name == menuId }
                ?.open(player)
        }
    }

    @CommandBody
    val print = subCommand {
        execute<CommandSender> { _, _, argument ->
            val menuId = argument.split(" ").getOrNull(1)
            menus
                .find { it.name == menuId }
                ?.toJson()?.let { println(it) }
        }
    }

    private fun reload() {
        menus.clear()
        val confs = File(bukkitPlugin.dataFolder, "workspace")
            .listRecursively()
            .filter { it.extension == "conf" }
            .map { HoconLoader.loadFromFile(it, destinationType = Type.JSON) }
            .filter { "menu" in it.getKeys(false) }

        confs.forEach { hocon ->
            debug("Loading ${hocon.name}")
            printCatching {
                menus += hocon.toMenu().also { it.name = hocon.name }
            }
        }

        debug("Loaded ${menus.size} menus")
    }

}