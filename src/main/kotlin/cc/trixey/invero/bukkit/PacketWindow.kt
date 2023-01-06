package cc.trixey.invero.bukkit

import cc.trixey.invero.common.WindowType

/**
 * @author Arasple
 * @since 2023/1/5 21:28
 */
abstract class PacketWindow(type: WindowType, title: String = "Untitled_Invero_Window") : BukkitWindow(type, title)