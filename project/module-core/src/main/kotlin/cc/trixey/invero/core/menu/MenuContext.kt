package cc.trixey.invero.core.menu

import cc.trixey.invero.core.Context

/**
 * Invero
 * cc.trixey.invero.core.menu.MenuContext
 *
 * @author Arasple
 * @since 2023/1/14 16:48
 */
interface MenuContext : Context {

    /**
     * 数组参数变量
     */
    fun getArguments(): Array<String>

    /**
     * 菜单路径
     */
    fun getMenuRoute(): List<String>

    /**
     * 自定义的临时变量
     */
    fun getVariables(): Map<String, String>

}