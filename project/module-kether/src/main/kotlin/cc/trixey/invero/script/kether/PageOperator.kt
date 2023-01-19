package cc.trixey.invero.script.kether

import cc.trixey.invero.common.panel.PagedPanel

/**
 * Invero
 * cc.trixey.invero.expansion.kether.menu.PageOperat
 *
 * @author Arasple
 * @since 2023/1/19 20:34
 */
enum class PageOperator {

    GET,

    GET_MAX,

    MODIFY,

    NEXT,

    PREVIOUS;

    fun isOutput(): Boolean {
        return this == GET || this == GET_MAX
    }

    fun run(panel: PagedPanel, value: Int) {
        when (this) {
            MODIFY -> panel.pageIndex = value
            NEXT -> panel.nextPage(value)
            PREVIOUS -> panel.previousPage(value)
            GET -> TODO()
            GET_MAX -> TODO()
        }
    }

}