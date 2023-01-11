package cc.trixey.invero.common.scroll

/**
 * Invero
 * cc.trixey.invero.common.scroll.ScrollDirection
 *
 * @author Arasple
 * @since 2023/1/11 13:35
 */
enum class ScrollDirection {

    VERTICAL,

    HORIZONTAL;

    val isVertical: Boolean
        get() = this == VERTICAL

    val isHorizontal: Boolean
        get() = this == HORIZONTAL

}