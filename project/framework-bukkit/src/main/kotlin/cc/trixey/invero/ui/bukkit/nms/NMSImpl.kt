package cc.trixey.invero.ui.bukkit.nms

import cc.trixey.invero.ui.common.ContainerType
import net.minecraft.server.v1_16_R3.*
import net.minecraft.world.inventory.Containers
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.unsafeInstance
import taboolib.module.nms.MinecraftVersion.isUniversal
import taboolib.module.nms.MinecraftVersion.majorLegacy
import taboolib.module.nms.sendPacketBlocking

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.nms.NMSImpl
 *
 * @author Arasple
 * @since 2022/10/20
 *
 * 总感觉他妈的发包容器轻飘飘的还迟钝 ??????????
 * TODO 究竟是什么发包问题
 */
class NMSImpl : NMS {

    private val itemAir = null.asNMSCopy()

    override fun sendWindowOpen(player: Player, containerId: Int, type: cc.trixey.invero.ui.common.ContainerType, title: String) {
        val instance = PacketPlayOutOpenWindow::class.java.unsafeInstance()

        when {
            isUniversal -> {
                val emptyTitle = CraftChatMessage.fromStringOrNull("§r§r")
                player.postPacket(
                    instance,
                    "containerId" to containerId,
                    if (majorLegacy < 11900) "type" to type.serialId
                    else "type" to Containers::class.java.getProperty<Containers<*>>(type.vanillaId, true),
                    "title" to (CraftChatMessage.fromStringOrNull(title) ?: emptyTitle)
                )
            }

            majorLegacy >= 11400 -> {
                player.postPacket(
                    instance, "a" to containerId, "b" to type.serialId, "c" to CraftChatMessage.fromStringOrNull(title)
                )
            }

            else -> {
                player.postPacket(
                    instance,
                    "a" to containerId,
                    "b" to type.bukkitId,
                    "c" to ChatComponentText(title),
                    "d" to type.containerSize - 1 // Fixed ViaVersion can not view 6x9 menu bug.
                )
            }
        }
    }

    override fun sendWindowClose(player: Player, containerId: Int) {
        player.postPacket(PacketPlayOutCloseWindow(containerId))
    }

    override fun sendWindowItems(player: Player, containerId: Int, itemStacks: List<ItemStack?>) {
        val instance = PacketPlayOutWindowItems::class.java.unsafeInstance()
        val items = itemStacks.asNMSCopy()

        when {
            majorLegacy >= 11701 -> {
                player.postPacket(
                    instance,
                    "containerId" to containerId,
                    "items" to items,
                    "carriedItem" to itemAir,
                    "stateId" to 1,
                )
            }

            else -> {
                player.postPacket(
                    instance,
                    (if (majorLegacy >= 11700) "containerId" else "a") to containerId,
                    if (majorLegacy >= 11000) "items" to items
                    else "items" to items.toTypedArray()
                )
            }
        }
    }

    override fun sendWindowSetSlot(player: Player, containerId: Int, slot: Int, itemStack: ItemStack?, stateId: Int) {
        when {
            majorLegacy >= 11701 -> {
                player.postPacket(
                    PacketPlayOutSetSlot::class.java.unsafeInstance(),
                    "containerId" to containerId,
                    "slot" to slot,
                    "itemStack" to itemStack.asNMSCopy(),
                    "stateId" to stateId,
                )
            }

            else -> {
                player.sendPacketBlocking(PacketPlayOutSetSlot(containerId, slot, itemStack.asNMSCopy()))
            }
        }
    }

    override fun sendWindowUpdateData(player: Player, containerId: Int, property: Int, value: Int) {
        TODO("Not yet implemented")
    }

    override fun asCraftMirror(itemStack: Any): ItemStack {
        return CraftItemStack.asCraftMirror(itemStack as net.minecraft.server.v1_16_R3.ItemStack?) as ItemStack
    }

    // TODO
    override fun getContainerId(player: Player): Int {
        player as CraftPlayer

        return if (isUniversal) {
            // 这里 NMS 翻译遇到些问题
//            Caused by: java.lang.NoSuchFieldError: containerMenu
//            player as org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
//            player.handle.containerMenu.containerId
            player.handle.getProperty<Container>("containerMenu")!!.getProperty<Int>("containerId")!!
        } else {
            player.handle.activeContainer.windowId
        }
    }

    private fun ItemStack?.asNMSCopy(): net.minecraft.server.v1_16_R3.ItemStack {
        return CraftItemStack.asNMSCopy(this)
    }

    private fun List<ItemStack?>.asNMSCopy(): List<net.minecraft.server.v1_16_R3.ItemStack> {
        return map { it.asNMSCopy() }
    }

}