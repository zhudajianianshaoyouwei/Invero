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
import taboolib.module.kether.*
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

/*
eco get
eco has 200
eco set 200
eco add 200
 */
@KetherParser(["eco", "money", "vault"], namespace = "invero", shared = true)
internal fun actionEco() = scriptParser {
    if (!it.hasNext()) actionNow { player().getBalance() }
    else actionFuture { future ->
        val method = it.nextToken()
        val action = if (it.hasNext()) newFrame(it.nextParsedAction()).run<Any>() else null

        if (action == null) future.complete(player().getBalance())

        action?.thenApply { output ->
            val money = output.cdouble
            when (method) {
                "get", "current" -> player().getBalance()
                "has" -> player().getBalance() >= money
                "rem", "take", "-=" -> player().withdrawBalance(money)
                "add", "give", "+=" -> player().depositBalance(money)
                else -> error("Unknown eco method: $method")
            }
        }
    }
}

@KetherParser(["playerpoints", "points"], namespace = "invero", shared = true)
internal fun actionPoints() = scriptParser {
    if (!it.hasNext()) actionNow { player().getBalance() }
    else actionFuture { future ->
        val method = it.nextToken()
        val action = if (it.hasNext()) newFrame(it.nextParsedAction()).run<Any>() else null

        if (action == null) future.complete(HookPlayerPoints.look(player()))


        // copy above code but change to playerpoints
        action?.thenApply { output ->
            val points = output.cint
            when (method) {
                "get", "current" -> HookPlayerPoints.look(player())
                "has" -> (HookPlayerPoints.look(player()) ?: 0) >= points
                "rem", "take", "-=" -> HookPlayerPoints.take(player(), points)
                "add", "give", "+=" -> HookPlayerPoints.add(player(), points)
                else -> error("Unknown points method: $method")
            }
        }

    }
}
