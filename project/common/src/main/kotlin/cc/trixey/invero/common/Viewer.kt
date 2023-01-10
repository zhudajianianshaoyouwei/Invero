package cc.trixey.invero.common

import java.util.*

/**
 * Invero
 * cc.trixey.invero.common.Viewer
 *
 * @author Arasple
 * @since 2022/12/20 20:35
 */
interface Viewer {

    val uuid: UUID

    fun isAvailable(): Boolean

    fun <T> getInstance(): T

    fun <T> getInstanceSafe(): T? = if (isAvailable()) getInstance() else null

}