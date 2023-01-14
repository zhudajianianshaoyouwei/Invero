package cc.trixey.invero.core.icon

/**
 * Invero
 * cc.trixey.invero.core.icon.Icon
 *
 * @author Arasple
 * @since 2023/1/14 14:02
 */
interface Icon {

    /**
     * 显示的物品结构体
     */
    val display: IconDisplay

    /**
     * 交互处理器
     */
    val handler: IconHandler

    /**
     * 主动翻译物品的相关变量的周期
     */
    val updateInterval: Int

    /**
     * 主动重定向有效子图标的周期
     */
    val relocateInterval: Int

    /**
     * 父级图标，可为空
     */
    val parent: Icon?

    /**
     * 子集图标，可为空
     */
    val children: List<Icon>?

}