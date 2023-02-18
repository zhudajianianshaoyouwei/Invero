package cc.trixey.invero.core.command.sub

import cc.trixey.invero.common.util.PasteResult.Status.ERROR
import cc.trixey.invero.common.util.PasteResult.Status.SUCCESS
import cc.trixey.invero.common.util.PasteVisibility
import cc.trixey.invero.common.util.createPaste
import cc.trixey.invero.common.util.paste
import cc.trixey.invero.core.serialize.ItemStackJsonSerializer
import cc.trixey.invero.core.util.prettyJson
import cc.trixey.invero.core.util.standardJson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.isAir
import taboolib.platform.util.sendLang
import taboolib.platform.util.serializeToByteArray
import taboolib.type.BukkitEquipment
import java.util.concurrent.TimeUnit

/**
 * Invero
 * cc.trixey.invero.core.command.sub.CommandItem
 *
 * @author Arasple
 * @since 2023/2/13 18:45
 */
object CommandItem {

    @CommandBody
    val encode = subCommand {
        dynamic("slot", optional = true) {
            suggest { BukkitEquipment.values().map { it.name.lowercase() } }

            execute<Player> { player, ctx, _ ->
                val equipment = BukkitEquipment.fromString(ctx["slot"])
                BukkitEquipment
                    .getItems(player)[equipment]
                    .postItemSerialization(player)
            }
        }
        execute<Player> { player, _, _ ->
            BukkitEquipment
                .getItems(player)[BukkitEquipment.HAND]
                .postItemSerialization(player)
        }
    }

    private fun ItemStack?.postItemSerialization(player: Player) {
        if (isAir) {
            player.sendLang("item-air")
            return
        }
        this!!

        submitAsync {
            val serialized = Json.encodeToJsonElement(ItemStackJsonSerializer, this@postItemSerialization).jsonObject
            val view = createPaste("Formatted Json View", prettyJson.encodeToString(serialized), "json")
            val base64 = createPaste("Use Base64", serializeToByteArray().toString(), "base64")
            val json = createPaste("Use Json", standardJson.encodeToString(serialized), "json")

            paste(
                "Invero Item Serialization",
                "item serialized to json format",
                PasteVisibility.UNLISTED,
                48,
                TimeUnit.HOURS,
                view,
                base64,
                json
            ).apply {
                when (status) {
                    SUCCESS -> player.sendLang("paste-success", anonymousLink)
                    ERROR -> player.sendLang("paste-failed")
                }
            }
        }
    }

}