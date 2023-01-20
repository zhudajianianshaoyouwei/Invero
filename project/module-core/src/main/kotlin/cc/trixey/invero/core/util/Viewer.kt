package cc.trixey.invero.core.util

import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.core.Session
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.core.util.Viewer
 *
 * @author Arasple
 * @since 2023/1/16 12:12
 */
inline val Viewer.session: Session
    get() = Session.get(this as PlayerViewer)

inline val Player.session: Session
    get() = Session.get(PlayerViewer(this))