package cc.trixey.invero.core

import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration

/**
 * Invero
 * cc.trixey.invero.core.InveroSettings
 *
 * @author Arasple
 * @since 2023/1/19 11:51
 */
object InveroSettings {

    @Config
    lateinit var conf: Configuration
        private set

    /**
     * 工作路径
     */
    @ConfigNode("Workspaces.paths")
    var workspaces = listOf<String>()
        private set

    /**
     * 文件名过滤
     */
    @ConfigNode("Workspaces.filter")
    var fileFilter = "^(?![#!]).*\\.(?i)(conf|hocon|yaml|yml|json)\$"
        private set

    /**
     * 物品默认名称的颜色
     */
    @ConfigNode("Menu.default-name-color")
    var defaultNameColor = "§7"
        private set

    /**
     * 物品默认描述的颜色
     */
    @ConfigNode("Menu.default-lore-color")
    var defaultLoreColor = "§7"
        private set

}