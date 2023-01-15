package cc.trixey.invero.core.animation

/**
 * TrMenu
 * cc.trixey.invero.core.animation.CycleMode
 *
 * @author Arasple
 * @since 2023/1/13 12:29
 */
enum class CycleMode {

    LOOP,

    REVERSABLE,

    RANDOM;

    val isLoop: Boolean
        get() = this == LOOP

    val isReversable: Boolean
        get() = this == REVERSABLE

    val isRandom: Boolean
        get() = this == RANDOM

}