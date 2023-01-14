package cc.trixey.invero.core.animation

/**
 * TrMenu
 * cc.trixey.invero.core.animation.Cycle
 *
 * @author Arasple
 * @since 2023/1/13 12:25
 */
interface Cycle<T> {

    fun get(): T

    fun getAndCycle(): T {
        return get().also { cycle() }
    }

    fun getMode(): CycleMode

    fun cycle()

}