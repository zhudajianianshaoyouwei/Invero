package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.supplier.sourceObject
import cc.trixey.invero.core.BaseMenu

/**
 * Invero
 * cc.trixey.invero.core.geneartor.GeneratorMenus
 *
 * @author Arasple
 * @since 2023/2/18 19:22
 */
class GeneratorMenus : BaseGenerator() {

    override fun generate() {
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
    }

}