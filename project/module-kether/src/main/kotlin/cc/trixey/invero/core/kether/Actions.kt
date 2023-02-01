package cc.trixey.invero.core.kether

import cc.trixey.invero.common.adventure.parseAndSendMiniMessage
import cc.trixey.invero.core.compat.bungeecord.Bungees
import cc.trixey.invero.core.compat.eco.HookPlayerPoints
import cc.trixey.invero.core.util.KetherHandler
import org.bukkit.entity.Player
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
 * cc.trixey.invero.core.kether.Actions
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
            val session = session()
            if (session != null)
                session.apply { viewer.get<Player>().sendMessage(parse(message, variableAs("@context"))) }
            else {
                val player = player()
                KetherHandler
                    .parseInline(message, player, variables().toMap())
                    .replacePlaceholder(player)
                    .parseAndSendMiniMessage(player)
            }
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
vault/eco/money

money
money has <amount>
money take <amount>
money give <amount>
 */
@KetherParser(["eco", "money", "vault"], namespace = "invero", shared = true)
internal fun actionEco() = combinationParser {
    it.group(
        symbol().option(),
        double().option()
    ).apply(it) { method, money ->
        if (money == null) {
            return@apply now { player().getBalance() }
        }

        when (method) {
            null, "current", "get" -> now { player().getBalance() }
            "take", "-=" -> now { player().withdrawBalance(money) }
            "give", "+=" -> now { player().depositBalance(money) }
            else -> error("Unknown eco method: $method")
        }
    }
}


/*
playerpoints
playerpoints has <amount>
playerpoints take <amount>
playerpoints give <amount>
playerpoints set <amount>
 */
@KetherParser(["playerpoints", "points"], namespace = "invero", shared = true)
internal fun actionPoints() = combinationParser {
    it.group(
        symbol().option(),
        int().option()
    ).apply(it) { method, amount ->
        if (amount == null) {
            return@apply now { HookPlayerPoints.look(player()) }
        }

        when (method) {
            null, "current", "get" -> now { HookPlayerPoints.look(player()) }
            "take", "-=" -> now { HookPlayerPoints.take(player(), amount) }
            "give", "+=" -> now { HookPlayerPoints.add(player(), amount) }
            "set" -> now { HookPlayerPoints.set(player(), amount) }
            else -> error("Unknown eco method: $method")
        }
    }
}
