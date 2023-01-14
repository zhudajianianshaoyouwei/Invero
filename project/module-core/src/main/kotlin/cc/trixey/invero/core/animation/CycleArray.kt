package cc.trixey.invero.core.animation

/**
 * TrMenu
 * cc.trixey.invero.core.animation.CycleString
 *
 * @author Arasple
 * @since 2023/1/13 12:26
 */
abstract class CycleArray<T>(private val value: Array<T>, private val mode: CycleMode = CycleMode.LOOP) : Cycle<T> {

    var index: Int = 0

    val maxIndex = value.indices.last

    var reversing: Boolean = false

    init {
        if (maxIndex == 0) error("Use CycleSingle instead of CycleArray for singled element")
    }

    override fun get(): T {
        return value[index]
    }

    override fun getMode(): CycleMode {
        return mode
    }

    override fun cycle() {
        if (mode.isRandom) {
            index = value.indices.random()
        } else if (mode.isLoop) {
            if (index == maxIndex) index = 0
            else index++
        } else if (mode.isReversable) {
            if (reversing) {
                index--
                if (index == 0) reversing = false
            } else {
                index++
                if (index == maxIndex) reversing = true
            }
        }
    }

}