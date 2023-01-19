package cc.trixey.invero.bukkit.util

import cc.trixey.invero.common.Viewer
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.bukkit.util.PacketCounter
 *
 * @author Arasple
 * @since 2023/1/19 22:36
 */
private val containerIdCounter = ConcurrentHashMap<UUID, Int>()

fun Viewer.countPacketContainerId(): Int {
    val id = getPacketContainerId()
    val newId = id % 100 + 1
    containerIdCounter[uuid] = newId
    return newId
}

fun Player.getPacketContainerId(): Int {
    return containerIdCounter.computeIfAbsent(uniqueId) { 0 }
}

fun Viewer.getPacketContainerId(): Int {
    return containerIdCounter.computeIfAbsent(uuid) { 0 }
}