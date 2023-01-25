package cc.trixey.invero.core

import cc.trixey.invero.core.menu.MenuBindings
import cc.trixey.invero.core.serialize.deserializeToMenu
import cc.trixey.invero.core.serialize.hocon.PatchedLoader
import cc.trixey.invero.core.util.listRecursively
import cc.trixey.invero.core.util.prettyPrint
import cc.trixey.invero.core.util.session
import org.bukkit.command.CommandSender
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submitAsync
import taboolib.common5.FileWatcher
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.onlinePlayers
import taboolib.platform.util.sendLang
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.InveroManager
 *
 * @author Arasple
 * @since 2023/1/19 11:37
 */
object InveroManager {

    private val defaultWorkspace = File(bukkitPlugin.dataFolder, "workspace")

    internal val menus = ConcurrentHashMap<String, Menu>()

    internal val bindings = ConcurrentHashMap<String, MenuBindings>()

    fun getMenu(name: String): Menu? {
        return menus[name]
    }

    fun getMenus(): Map<String, Menu> {
        return menus
    }

    @Awake(LifeCycle.ACTIVE)
    fun invoke() = load(console().cast())

    fun load(sender: CommandSender) {
        val workspaces = getWorkspaces()
        val start = System.currentTimeMillis()

        // 注销旧菜单
        if (menus.isNotEmpty()) {
            // TODO 改善 RELOAD 体验
            onlinePlayers.forEach { player -> player.session?.menu?.close(player) }
            menus.values.forEach { it.unregister() }
            menus.clear()
        }
        // 获取工作空间
        if (workspaces.isEmpty()) {
            return sender.sendLang("menu-loader-workspace-empty")
        } else {
            // 初始化工作路径
            sender.sendLang("menu-loader-workspace-inited", workspaces.size)
            // 匹配规范文件
            val matcher = InveroSettings.fileFilter.toRegex()
            // 匹配经声明的菜单配置
            val configurations = workspaces
                .flatMap { it.listRecursively() }
                .filter { it.name.matches(matcher) }
                .mapNotNull {
                    runCatching {
                        val conf = PatchedLoader.loadFromFile(it)
                        if ("menu" in conf.getKeys(false)) conf else null
                    }.onFailure { failure ->
                        failure.prettyPrint()
                        sender.sendLang("menu-loader-file-errored", it.name)
                    }.getOrNull()
                }
            // 载入内存
            configurations.forEach { conf ->
                val file = conf.file ?: error("No file is found for conf ${conf.name}")
                val menu = runCatching { conf.deserializeToMenu(conf.name) }
                    .onFailure {
                        it.prettyPrint()
                        sender.sendLang("menu-loader-menu-errored", file.path ?: conf.name)
                    }
                    .getOrNull()

                if (menu != null) {
                    if (conf.name in menus.keys) {
                        sender.sendLang("menu-loader-menu-duplicate", file.path ?: conf.name)
                    } else {
                        menus[menu.name!!] = menu
                        registerService(file, menu)
                    }
                }
            }
            // 输出载入信息
            if (menus.isNotEmpty()) {
                val took = (System.currentTimeMillis() - start).div(1000.0)
                sender.sendLang("menu-loader-menu-finished", menus.size, took)
            }
        }
    }

    private fun registerService(file: File, menu: Menu) {
        val menuId = menu.name!!

        if (FileWatcher.INSTANCE.hasListener(file)) return
        FileWatcher.INSTANCE.addSimpleListener(file) {
            submitAsync {
                if (!file.exists() || !menus.containsKey(menuId)) {
                    FileWatcher.INSTANCE.removeListener(file)
                } else runCatching {
                    PatchedLoader.loadFromFile(file).deserializeToMenu(name = menuId)
                }.onFailure {
                    it.prettyPrint()
                    console().cast<CommandSender>().sendLang("menu-loader-auto-reload-errored", menu.name!!)
                }.getOrNull()?.let { loaded ->
                    // replace in memory
                    menus[menuId]?.unregister()
                    menus[menuId] = loaded
                    // auto open
                    onlinePlayers
                        .filter { it.session?.menu?.name == menuId }
                        .also {
                            if (it.isNotEmpty())
                                console().cast<CommandSender>().sendLang("menu-loader-auto-reload-successed", menuId)
                        }
                        .forEach { loaded.open(player = it) }
                }
            }
        }
    }

    fun getWorkspaces(): List<File> {
        val configurated = InveroSettings.workspaces
        return if (configurated.isEmpty()) {
            listOf(defaultWorkspace)
        } else {
            configurated.map { path -> File(path) }
        }.filter { it.isDirectory }
    }

}