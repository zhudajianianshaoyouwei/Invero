package cc.trixey.invero.core.icon

import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Invero
 * cc.trixey.invero.core.icon.Slot
 *
 * @author Arasple
 * @since 2023/1/16 10:49
 */
@Serializable
class Slot(private val value: String) {

    @Transient
    private val uncalculatedSlots = mutableSetOf<Int>()

    // 1~10 or 1-10 ->> Slots: 1,2,3,4,5,6,7,8,9,10
    // 1;2;3 ->> Slots: 1,2,3
    // 0x2;0x3 ->> Slots (x=0,y=2), (x=0,y=3)
    private val locations by lazy {
        value
            .split(";")
            .mapNotNull {
                // range in slot
                if ('~' in it || '-' in it || ".." in it) {
                    val (from, to) = it.split("~", "-", "..")
                    uncalculatedSlots += (from.toInt() to to.toInt()).toList().toSet()
                } else if ('x' in it) {
                    val (x, y) = it.split('x')
                    return@mapNotNull Pos(x.toInt() to y.toInt())
                }

                return@mapNotNull null
            }
    }

    fun release(scale: Scale) = uncalculatedSlots
        .filter { it >= 0 }
        .map { scale.convertToPosition(it) } + locations

}