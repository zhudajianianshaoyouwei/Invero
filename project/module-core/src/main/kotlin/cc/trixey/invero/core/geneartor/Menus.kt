package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.supplier.Object
import cc.trixey.invero.common.supplier.sourceObject
import cc.trixey.invero.core.BaseMenu

/**
 * Invero
 * cc.trixey.invero.core.geneartor.Menus
 *
 * @author Arasple
 * @since 2023/2/18 19:22
 */
class Menus : BaseGenerator() {

    override fun generate(): List<Object> {
        generated = Invero.API.getMenuManager().getMenus().map {
            val menu = it as BaseMenu

            sourceObject {
                put("id", menu.id)
                put("type", menu.settings.containerType.name)
                put("rows", menu.settings.rows)
                put("virtual", menu.settings.virtual)
                put("panels", menu.panels.size)
            }
        }
        return generated!!
    }

}