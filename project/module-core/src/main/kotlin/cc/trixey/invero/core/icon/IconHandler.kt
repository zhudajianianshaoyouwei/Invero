package cc.trixey.invero.core.icon

import cc.trixey.invero.common.event.ClickType
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.action.Action
import cc.trixey.invero.core.util.letCatching
import kotlinx.serialization.Serializable
import taboolib.common.platform.function.submitAsync

/**
 * Invero
 * cc.trixey.invero.core.icon.IconHandler
 *
 * @author Arasple
 * @since 2023/1/18 11:14
 */
@Serializable
class IconHandler(val all: Action?, val response: Map<ClickType, Action> = mapOf()) {

    fun run(context: Context, clickType: ClickType) = submitAsync {
        letCatching {
            all?.run(context)?.get()
            response[clickType]?.run(context)?.get()
        }
    }

}