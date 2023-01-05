package cc.trixey.invero.bukkit

import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.WindowType

/**
 * @author Arasple
 * @since 2022/12/29 12:43
 */
abstract class BaseWindow(
    val type: WindowType,
    title: String = "Untitled_Invero_Window",
) : Window {

    override var title: String = title
        set(value) {
            field = value
            // TODO Packet-Title-Modify
        }

    override val viewers: MutableSet<Viewer> = mutableSetOf()

    override val panels: ArrayList<Panel> = arrayListOf()

    override val size: Int = type.entireWindowSize

}