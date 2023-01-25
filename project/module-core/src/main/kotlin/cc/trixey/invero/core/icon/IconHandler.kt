package cc.trixey.invero.core.icon

import cc.trixey.invero.common.event.ClickType
import cc.trixey.invero.core.action.Action
import kotlinx.serialization.Serializable

/**
 * Invero
 * cc.trixey.invero.core.icon.IconHandler
 *
 * @author Arasple
 * @since 2023/1/18 11:14
 */
@Serializable
class IconHandler(val all: Action?, val response: Map<ClickType, Action> = mapOf())