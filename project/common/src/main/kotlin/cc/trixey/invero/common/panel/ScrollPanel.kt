package cc.trixey.invero.common.panel

import cc.trixey.invero.common.scroll.ScrollDirection
import cc.trixey.invero.common.scroll.ScrollTail

/**
 * Invero
 * cc.trixey.invero.common.panel.ScrollPanel
 *
 * @author Arasple
 * @since 2023/1/7 21:28
 */
interface ScrollPanel {

    val direction: ScrollDirection

    val tail: ScrollTail

}