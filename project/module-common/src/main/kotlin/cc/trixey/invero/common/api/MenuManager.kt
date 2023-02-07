package cc.trixey.invero.common.api

import cc.trixey.invero.common.Menu
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.module.configuration.Configuration
import java.io.File

/**
 * Invero
 * cc.trixey.invero.common.api.MenuManager
 *
 * @author Arasple
 * @since 2023/2/1 16:57
 */
interface MenuManager {

    /**
     * 取得特定 ID 的菜单
     */
    fun getMenu(id: String, ignoreCase: Boolean = false): Menu?

    /**
     * 取得已加载的所有菜单
     */
    fun getMenus(): List<Menu>

    /**
     * 加载菜单
     *
     * @param workspace 即可是菜单配置文件，也可是工作目录
     */
    fun deserialize(workspace: File): List<SerializeResult>

    /**
     * (插件默认) 的重载菜单
     */
    fun reload(receiver: CommandSender = console().cast())

    /**
     * 初始化菜单绑定
     */
    fun initMenuBindings(menu: Menu)

    /**
     * 将配置反序列化为菜单
     */
    fun deserializeToMenu(configuration: Configuration, name: String? = null): Menu

    /**
     * 菜单序列化为 JSON
     */
    fun serializeToJson(menu: Menu): String

    /**
     * 寻找符合绑定条件的菜单
     */
    fun findBound(itemStack: ItemStack): Menu?

    /**
     * 寻找符合绑定条件的菜单
     */
    fun findBound(chat: String): Menu?

    /**
     * 取得 JSON 序列化器
     *
     * @return kotlinx.serialization.json
     */
    fun <R> getJsonSerializer(): R

}