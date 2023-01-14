package cc.trixey.invero.core.impl.icon

import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.icon.IconDisplay
import cc.trixey.invero.core.icon.IconHandler

/**
 * Invero
 * cc.trixey.invero.core.impl.icon.IconStandard
 *
 * @author Arasple
 * @since 2023/1/14 18:03
 */
class IconStandard(
    override val display: IconDisplay,
    override val handler: IconHandler,
    override val updateInterval: Int,
    override val relocateInterval: Int,
    override val parent: Icon?,
    override val children: List<Icon>?
) : Icon {



}