package cc.trixey.invero.core.geneartor

/**
 * Invero
 * cc.trixey.invero.core.geneartor.Generator
 *
 * @author Arasple
 * @since 2023/1/29 21:56
 */
interface Generator {

    var generated: List<Object>?

    /**
     * 初始化元素（缓存）
     */
    fun generate(): List<Object>

    /**
     * 过滤
     */
    fun filter(block: (Object) -> Boolean): Generator

    /**
     * 排序
     */
    fun <R : Comparable<R>> sort(block: (Object) -> R): Generator

    /**
     * 当前有效元素是否为空
     */
    fun isEmpty(): Boolean

}