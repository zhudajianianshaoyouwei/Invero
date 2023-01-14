package cc.trixey.invero.core.util

/**
 * Invero
 * cc.trixey.invero.core.util.ObjectConvertor
 *
 * @author Arasple
 * @since 2023/1/14 12:51
 */
fun Any?.safeBoolean(): Boolean {
    return if (this == null) false
    else if (this is Boolean) this
    else !toString().equals("false", true)
}