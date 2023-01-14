package cc.trixey.invero.core.menu

import cc.trixey.invero.common.Pos

/**
 * Invero
 * cc.trixey.invero.core.menu.Layout
 *
 * @author Arasple
 * @since 2023/1/14 17:00
 */
interface Layout {

    fun search(key: Char) = search(key.toString())

    fun search(key: String): Set<Pos>

}