@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.action

import cc.trixey.invero.core.Context
import cc.trixey.invero.core.InveroManager
import cc.trixey.invero.core.serialize.ListStringSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNames
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.action.FunctionalActionCatcher
 *
 * @author Arasple
 * @since 2023/1/31 21:10
 */
@Serializable
class FunctionalActionCatcher(
    @JsonNames("catch", "key")
    val id: String,
    val content: JsonElement?,
    @JsonNames("signIndex", "index", "line")
    val signLine: Int?,
    @Serializable(ListStringSerializer::class)
    val cancel: List<String>?,
    val beforeInput: Action?,
    val afterInput: Action,
    val onRepeat: Action?,
    val onCancel: Action?,
) : Action() {

    @Transient
    private val inputCatcher = InputCatcher(id, content, signLine, cancel, beforeInput, afterInput, onRepeat, onCancel)

    override fun run(context: Context): CompletableFuture<Boolean> {
        val session = context.session ?: error("FunctionalActionCatcher can only be used when there is a valid session")
        val menu = session.menu.name
        val player = context.viewer.get<Player>()
        // close window
        session.menu.close(player)
        // input
        submitAsync(delay = 2L) {
            inputCatcher.run(player, context) {
                if (menu != null) InveroManager.getMenu(menu)?.open(player, context.variables)
            }
        }
        return CompletableFuture.completedFuture(false)
    }

}