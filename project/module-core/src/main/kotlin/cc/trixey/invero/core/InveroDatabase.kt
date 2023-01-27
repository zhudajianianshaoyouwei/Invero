package cc.trixey.invero.core

import cc.trixey.invero.core.util.prettyPrint
import org.bukkit.command.CommandSender
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.expansion.*
import taboolib.platform.util.sendLang
import java.util.*


/**
 * Invero
 * cc.trixey.invero.core.InveroDatabase
 *
 * @author Arasple
 * @since 2023/1/27 15:46
 */
object InveroDatabase {

    val globalDataDatabase by lazy {
        val randUUID = UUID.randomUUID()
        playerDataContainer[randUUID] = DataContainer("@global_data", playerDatabase!!)
        playerDataContainer[randUUID]!!
    }

    @Awake(LifeCycle.ACTIVE)
    fun setup() {
        val type = Type.values().find { it.name.equals(InveroSettings.databaseType, true) } ?: Type.SQLITE

        runCatching {
            when (type) {
                Type.SQLITE -> setupPlayerDatabase()
                Type.SQL -> setupPlayerDatabase(InveroSettings.sqlSection ?: error("No valid sql section configurated"))
            }
        }.onFailure {
            it.prettyPrint()
            console().cast<CommandSender>().sendLang("database-errored", type.name)
        }.onSuccess {
            console().cast<CommandSender>().sendLang("database-connected", type.name)
        }.getOrNull()
    }

    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        e.player.setupDataContainer()
    }

    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        e.player.releaseDataContainer()
    }

    enum class Type {

        SQLITE,

        SQL

    }

}