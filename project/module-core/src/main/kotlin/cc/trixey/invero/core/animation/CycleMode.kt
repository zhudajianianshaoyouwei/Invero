package cc.trixey.invero.core.animation

import cc.trixey.invero.serialize.SerializerCycleMode
import kotlinx.serialization.Serializable

/**
 * TrMenu
 * cc.trixey.invero.core.animation.CycleMode
 *
 * @author Arasple
 * @since 2023/1/13 12:29
 */
@Serializable(with = SerializerCycleMode::class)
enum class CycleMode {

    ONE_WAY,

    LOOP,

    REVERSABLE,

    RANDOM;

    val isOneway: Boolean
        get() = this == ONE_WAY

    val isLoop: Boolean
        get() = this == LOOP

    val isReversable: Boolean
        get() = this == REVERSABLE

    val isRandom: Boolean
        get() = this == RANDOM

    companion object {

        fun of(name: String): CycleMode {
            return values().find { it.name.equals(name, true) } ?: LOOP
        }

    }

}