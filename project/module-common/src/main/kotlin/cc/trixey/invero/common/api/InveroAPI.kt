package cc.trixey.invero.common.api

import cc.trixey.invero.common.supplier.ElementGenerator
import cc.trixey.invero.common.supplier.ItemSourceProvider

/**
 * Invero
 * cc.trixey.invero.common.api.InveroAPI
 *
 * @author Arasple
 * @since 2023/2/1 16:37
 */
interface InveroAPI {

    /**
     * 取得菜单管理器
     */
    fun getMenuManager(): MenuManager

    /**
     * 取得数据管理器
     */
    fun getDataManager(): DataManager

    /**
     * 注册一个类型的元素生成器
     */
    fun registerElementGenerator(name: String, provider: ElementGenerator)

    /**
     * 根据名称引用创建一个元素生成器
     */
    fun createElementGenerator(name: String): ElementGenerator

    /**
     * 注册一个物品源提供器
     */
    fun registerItemSourceProvider(name: String, provider: ItemSourceProvider)

    /**
     * 取得物品源提高器
     */
    fun getItemSourceProvider(name: String): ItemSourceProvider?

}