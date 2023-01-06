package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2023/1/5 23:11
 */
interface PagedPanel : Panel {

    var pageIndex: Int

    fun nextPage(value: Int = 1) {
        pageIndex += value
    }

    fun previousPage(value: Int = 1) {
        pageIndex -= value
    }

    fun switchPage(index: Int) {
        pageIndex = index
    }

}