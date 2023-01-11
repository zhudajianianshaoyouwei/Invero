package cc.trixey.invero.common.panel

import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.scroll.ScrollDirection
import cc.trixey.invero.common.scroll.ScrollTail

/**
 * Invero
 * cc.trixey.invero.common.panel.ScrollPanel
 *
 * @author Arasple
 * @since 2023/1/7 21:28
 */
interface ScrollPanel : Panel {

    val direction: ScrollDirection

    val tail: ScrollTail

    val visibleColumsSize: Int
        get() = if (direction.isVertical) scale.height else scale.width

    val columCapacity: Int
        get() = if (direction.isVertical) scale.width else scale.height


}