package cc.trixey.invero.core.session

import cc.trixey.invero.core.Context
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.session.MenuContext
 *
 * @author Arasple
 * @since 2023/1/15 22:41
 */
class MenuContext(
    private var arguments: Array<String> = arrayOf(),
    private val menuRoute: ArrayList<String> = arrayListOf(),
    private val variables: ConcurrentHashMap<String, String> = ConcurrentHashMap()
) : Context {

    fun getArguments(): Array<String> {
        return arguments
    }

    fun getMenuRoute(): List<String> {
        return menuRoute
    }

    fun getVariables(): Map<String, String> {
        return variables
    }

}