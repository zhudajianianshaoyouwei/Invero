package cc.trixey.invero.impl

import cc.trixey.invero.core.menu.MenuContext
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.impl.DefaultMenuContext
 *
 * @author Arasple
 * @since 2023/1/14 16:52
 */
class DefaultMenuContext(
    private var arguments: Array<String> = arrayOf(),
    private val menuRoute: ArrayList<String> = arrayListOf(),
    private val variables: ConcurrentHashMap<String, String> = ConcurrentHashMap()
) : MenuContext {

    override fun getArguments(): Array<String> {
        return arguments
    }

    override fun getMenuRoute(): List<String> {
        return menuRoute
    }

    override fun getVariables(): Map<String, String> {
        return variables
    }

}