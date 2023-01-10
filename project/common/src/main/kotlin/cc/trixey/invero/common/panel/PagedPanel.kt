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
        (pageIndex + value).assertSwitch()
    }

    fun previousPage(value: Int = 1) {
        (pageIndex - value).assertSwitch()
    }

    fun switchPage(index: Int) {
        pageIndex = index
    }

    fun Int.assertSwitch() {
        if (this in 0..maxPageIndex) {
            switchPage(this)
        }
    }

}