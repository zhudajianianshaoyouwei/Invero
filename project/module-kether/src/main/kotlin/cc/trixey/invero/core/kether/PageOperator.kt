package cc.trixey.invero.core.kether

import cc.trixey.invero.ui.common.panel.PagedPanel

/**
 * Invero
 * cc.trixey.invero.expansion.kether.menu.PageOperat
 *
 * @author Arasple
 * @since 2023/1/19 20:34
 */
enum class PageOperator(val aliases: Set<String> = setOf()) {

    GET(setOf("get", "current")),

    GET_MAX(setOf("max")),

    IS_FIRST_PAGE(setOf("isFirst")),

    IS_LAST_PAGE(setOf("isLast")),

    MODIFY(setOf("set", "to", "switch")),

    NEXT(setOf("next", "add", "increase", "+")),

    PREVIOUS(setOf("previous", "sub", "decrease", "-"));

    fun isOutput(): Boolean {
        return this == GET || this == GET_MAX || this == IS_FIRST_PAGE || this == IS_LAST_PAGE
    }

    fun invoke(panel: PagedPanel, value: Int) {
        when (this) {
            MODIFY -> panel.pageIndex = value
            NEXT -> panel.nextPage(value)
            PREVIOUS -> panel.previousPage(value)
            else -> error("out of case")
        }
    }

    companion object {

        fun of(name: String): PageOperator {
            return values().find { it.aliases.any { ali -> ali.equals(name, true) } } ?: GET
        }

    }

}