package cc.trixey.invero.core.script.kether

import cc.trixey.invero.common.adventure.parseAndSendMiniMessage
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.compat.bungeecord.Bungees
import cc.trixey.invero.core.compat.eco.HookPlayerPoints
import cc.trixey.invero.core.script.contextVar
import cc.trixey.invero.core.script.player
import cc.trixey.invero.core.script.session
import cc.trixey.invero.core.util.KetherHandler
import org.bukkit.entity.Player
import taboolib.common5.cdouble
import taboolib.common5.cint
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.platform.compat.depositBalance
import taboolib.platform.compat.getBalance
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.compat.withdrawBalance
import taboolib.platform.util.onlinePlayers
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.script.Actions
 *
 * @author Arasple
 * @since 2023/1/24 22:51
 */

/*
msg <action>
 */
@KetherParser(["message", "msg"], namespace = "invero", shared = true)
internal fun actionMessage() = combinationParser {
    it.group(
        text(),
    ).apply(it) { message ->
        now {
            val context = contextVar<Context?>("@context")?.variables ?: variables().toMap()
            val player = player()
            KetherHandler
                .parseInline(message, player, context)
                .replacePlaceholder(player)
                .parseAndSendMiniMessage(player)
        }
    }
}

/*
connect <serverName> for <playerName>
 */
@KetherParser(["connect", "bungee"], namespace = "invero", shared = true)
internal fun actionConnect() = combinationParser {
    it.group(
        text(),
        command("for", then = action()).option().defaultsTo(null)
    ).apply(it) { server, player ->
        future {
            if (player == null) {
                session()?.viewer?.get<Player>()?.let { player ->
                    CompletableFuture.completedFuture(Bungees.connect(player, server))
                } ?: CompletableFuture.completedFuture(null)
            } else {
                newFrame(player).run<Any>().thenApply { playerId ->
                    onlinePlayers
                        .find { p -> p.name == playerId }
                        ?.let { p -> Bungees.connect(p, server) }
                }
            }
        }
    }
}

@KetherParser(["eco", "money", "vault"], namespace = "invero", shared = true)
internal fun actionEco() = combinationParser {
    it.group(
        symbol().option(),
        action().option()
    ).apply(it) { method, action ->

        now {
            if (action == null) return@now player().getBalance()
            val money = newFrame(action).run<Any>().get().cdouble
            when (method) {
                null, "current", "get" -> player().getBalance()
                "take", "-=" -> player().withdrawBalance(money)
                "give", "+=" -> player().depositBalance(money)
                else -> error("Unknown eco method: $method")
            }
        }
    }
}

@KetherParser(["playerpoints", "points"], namespace = "invero", shared = true)
internal fun actionPoints() = combinationParser {
    it.group(
        symbol().option(),
        action().option()
    ).apply(it) { method, action ->
        now {
            if (action == null) return@now HookPlayerPoints.look(player())
            val amount = newFrame(action).run<Any>().get().cint
            when (method) {
                null, "current", "get" -> HookPlayerPoints.look(player())
                "take", "-=" -> HookPlayerPoints.take(player(), amount)
                "give", "+=" -> HookPlayerPoints.add(player(), amount)
                "set" -> HookPlayerPoints.set(player(), amount)
                else -> error("Unknown eco method: $method")
            }
        }
    }
}
