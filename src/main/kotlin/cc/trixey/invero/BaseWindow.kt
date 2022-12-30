package cc.trixey.invero

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

    override fun locate(x: Int, y: Int): Int {
        return when {
            type.isOrdinaryChest -> y * 9 + x
            type == WindowType.GENERIC_3X3 -> {
                if (y >= 3) 3 * 3 + y * 9 + x
                else y * 3 + x
            }

            else -> TODO("Not yet implemented")
        }
    }

    override fun locatePanel(panel: Panel): Set<Int> {
        if (panel in panels) {
            val locate = panel.locate
            val index = if (locate.isPureSlot) locate.pureSlot
            else locate.locate.let { locate(it.first, it.second) }
            val (width, height) = panel.scale


        }
        return setOf()
    }

}