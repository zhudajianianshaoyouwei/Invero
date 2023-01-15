package cc.trixey.invero.core.icon

import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.core.action.Condition
import cc.trixey.invero.core.item.IconUpdatable
import cc.trixey.invero.core.item.MenuElement
import cc.trixey.invero.core.menu.MenuSession
import cc.trixey.invero.core.menu.PanelAgent

/**
 * Invero
 * cc.trixey.invero.core.icon.Icon
 *
 * @author Arasple
 * @since 2023/1/14 14:02
 */
interface Icon {

    /**
     * 条件
     */
    val condition: Condition?

    /**
     * 交互处理器
     */
    val handler: IconHandler

    /**
     * 主动翻译物品的相关变量的周期
     */
    val updateInterval: Int

    /**
     * 主动翻译物品的相关变量的类型
     */
    val updateProperties: Set<IconUpdatable>

    /**
     * 主动重定向有效子图标的周期
     */
    val relocateInterval: Int

    /**
     * 定位
     */
    val slots: Set<Pos>?

    /**
     * 子图标
     */
    val subIcons: List<Icon>?

    /**
     * 父级图标
     */
    val parent: Icon?

    /**
     * 创建
     */
    fun create(session: MenuSession, panelAgent: PanelAgent<*>, panel: ElementalPanel): MenuElement

}