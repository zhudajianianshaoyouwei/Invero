package cc.trixey.invero.common.panel

import cc.trixey.invero.common.Panel

/**
 * @author Arasple
 * @since 2023/1/5 23:11
 */
interface PagedPanel : Panel {

    val maxPageIndex: Int

    var pageIndex: Int

    fun nextPage(value: Int = 1) {
        (pageIndex + value).assert { switchPage(it) }
    }

    fun previousPage(value: Int = 1) {
        (pageIndex - value).assert { switchPage(it) }
    }

    fun switchPage(index: Int) {
        pageIndex = index
    }

    fun Int.assert(block: (Int) -> Unit) {
        if (this >= 0 && this <= maxPageIndex) block(this)
    }

}