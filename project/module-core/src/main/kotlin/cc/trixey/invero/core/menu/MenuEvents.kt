@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.menu

import cc.trixey.invero.core.action.Action
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Invero
 * cc.trixey.invero.core.menu.MenuEvents
 *
 * @author Arasple
 * @since 2023/1/25 11:36
 */
@Serializable
class MenuEvents(
    @JsonNames("pre_open", "open")
    val preOpen: Action? = null,
    @JsonNames("post_open", "opened")
    val postOpen: Action? = null,
    @JsonNames("close")
    val close: Action? = null,
)