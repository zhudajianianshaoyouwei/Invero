package cc.trixey.invero.plugin

import cc.trixey.invero.core.util.fluentMessage
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.platform.util.buildItem
import taboolib.platform.util.modifyLore

/**
 * Invero
 * cc.trixey.invero.plugin.InveroPlugin
 *
 * @author Arasple
 * @since 2022/12/20 20:42
 */
object InveroPlugin : Plugin() {

    override fun onLoad() {
        info("Loaded")

        repeat(1000) { hot() }
    }

    fun hot() {
        val item = buildItem(XMaterial.PLAYER_HEAD) {
            repeat(10) { lore += "<red><gradient>{{text}}" }
        }
        val tag = ItemTag()
        tag["SkullOwner"] = ItemTagData.toNBT("Arasple")
        tag.putAll(item.getItemTag())
        tag.saveTo(item)

        item.modifyLore { replaceAll { it.fluentMessage() } }
    }

}