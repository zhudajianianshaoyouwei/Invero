package cc.trixey.invero.core.menu

import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.Window
import cc.trixey.invero.core.icon.Icon

/**
 * Invero
 * cc.trixey.invero.core.menu.PanelAgent
 *
 * @author Arasple
 * @since 2023/1/14 16:58
 */
interface PanelAgent<T : Panel> {

    val scale: Scale

    val layout: Layout

    val icons: List<Icon>

    val locate: Pos?

    fun apply(session: MenuSession)

}