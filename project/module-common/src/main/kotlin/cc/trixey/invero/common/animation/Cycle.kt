package cc.trixey.invero.common.animation

/**
 * TrMenu
 * cc.trixey.invero.common.animation.Cycle
 *
 * @author Arasple
 * @since 2023/1/13 12:25
 */
interface Cycle<T> {

    val mode: CycleMode

    fun get(): T

    fun getAndCycle(): T {
        return get().also { cycle() }
    }

    fun cycle()

}