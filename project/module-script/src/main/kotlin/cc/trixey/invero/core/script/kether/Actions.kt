package cc.trixey.invero.core.script.kether

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.compat.bungeecord.Bungees
import cc.trixey.invero.core.compat.eco.HookPlayerPoints
import cc.trixey.invero.core.script.contextVar
import cc.trixey.invero.core.script.player
import cc.trixey.invero.core.script.session
import cc.trixey.invero.core.util.fluentMessageComponent
import org.bukkit.entity.Player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.actionNow
import taboolib.module.kether.combinationParser
import taboolib.module.kether.scriptParser
import taboolib.platform.compat.depositBalance
import taboolib.platform.compat.getBalance
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

            message.fluentMessageComponent(player, context, send = true)
        }
    }
}

@KetherParser(["parse"], namespace = "invero", shared = true)
internal fun actionParse() = combinationParser {
    it.group(
        text(),
    ).apply(it) { message ->
        now {
            val context = contextVar<Context?>("@context")?.variables ?: variables().toMap()
            val player = player()

            message
                .fluentMessageComponent(player, context)
                .toLegacyText()
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

/**
 * eco
 * eco get
 * eco take 200
 * eco give 200
 * eco set 200
 */
@KetherParser(["eco", "money", "vault"], namespace = "invero", shared = true)
internal fun actionEco() = scriptParser {
    if (!it.hasNext()) actionNow { player().getBalance() }
    else {
        val method = it.nextToken()
        if (method == null || method == "get" || method == "balance") actionNow { player().getBalance() }
        else {
            val money = it.nextDouble()
            when (method) {
                "has" -> actionNow { player().getBalance() >= money }
                "take" -> actionNow { player().withdrawBalance(money) }
                "give" -> actionNow { player().depositBalance(money) }
                else -> error("Unknown eco method: $method")
            }
        }
    }
}

@KetherParser(["playerpoints", "points"], namespace = "invero", shared = true)
internal fun actionPoints() = scriptParser {
    if (!it.hasNext()) actionNow { HookPlayerPoints.look(player()) }
    else {
        val method = it.nextToken()
        if (method == null || method == "get" || method == "balance") actionNow {
            HookPlayerPoints.look(player()) ?: -1
        }
        else {
            val points = it.nextInt()
            when (method) {
                "has" -> actionNow { (HookPlayerPoints.look(player()) ?: 0) >= points }
                "take" -> actionNow { HookPlayerPoints.take(player(), points) }
                "give" -> actionNow { HookPlayerPoints.add(player(), points) }
                else -> error("Unknown playerpoints method: $method")
            }
        }
    }
}