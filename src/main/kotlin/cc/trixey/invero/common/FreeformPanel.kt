package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2023/1/6 14:57
 */
interface FreeformPanel : Panel {

    val viewport: Pos

    fun reset()

    fun shift(x: Int = 0, y: Int = 0)

    fun left() = shift(-1)

    fun right() = shift(1)

    fun up() = shift(y = -1)

    fun down() = shift(y = 1)

    fun upLeft() = shift(-1, -1)

    fun upRight() = shift(1, -1)

    fun downLeft() = shift(-1, 1)

    fun downRight() = shift(1, 1)

    override fun wipe(wiping: Set<Pos>, absolute: Boolean) {
        run {
            if (!absolute) wiping
                .filterNot { scale.isOutOfBounds(it.x, it.y, -viewport) }
                .map { it - viewport }
                .toSet()
            else wiping
        }.let {
            super.wipe(it, absolute)
        }
    }

}