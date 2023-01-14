package cc.trixey.invero.core.action

/**
 * Invero
 * cc.trixey.invero.core.action.Comparor
 *
 * @author Arasple
 * @since 2023/1/14 12:45
 */
interface Comparator {

    fun getType(): Type

    fun getValue(): Any?

    fun matches(input: Any?): Boolean

    fun isNumberic(): Boolean

    fun isString(): Boolean

    enum class Type {

        GREATER,

        GREATER_OR_EQUALS,

        SMALLER,

        SMALLER_OR_EQUALS,

        EQUALS

    }

}