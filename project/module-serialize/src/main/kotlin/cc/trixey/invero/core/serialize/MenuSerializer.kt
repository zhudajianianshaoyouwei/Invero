package cc.trixey.invero.core.serialize

import cc.trixey.invero.core.menu.Menu
import cc.trixey.invero.core.menu.PanelAgent
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration

/**
 * Invero
 * cc.trixey.invero.core.serialize.MenuSerializer
 *
 * @author Arasple
 * @since 2023/1/14 21:30
 */
interface MenuSerializer<T : Menu> {

    fun load(conf: Configuration): T

    fun loadPanel(section: ConfigurationSection): PanelAgent<*>

}