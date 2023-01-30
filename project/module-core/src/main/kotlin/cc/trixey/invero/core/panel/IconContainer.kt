package cc.trixey.invero.core.panel

import cc.trixey.invero.core.icon.Icon

/**
 * Invero
 * cc.trixey.invero.core.panel.Iconed
 *
 * @author Arasple
 * @since 2023/1/30 14:47
 */
interface IconContainer {

    val icons: Map<String, Icon>

    fun registerIcons() {
        icons.forEach { (name, icon) ->
            if (icon.id == null) icon.id = name
            icon.subIcons?.forEach {
                it.parent = icon
                it.id = icon.id
            }
        }
    }

}