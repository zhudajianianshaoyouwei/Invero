package cc.trixey.invero.impl

import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.core.action.Condition
import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.icon.IconHandler
import cc.trixey.invero.core.item.IconUpdatable
import cc.trixey.invero.core.item.ItemProperty
import cc.trixey.invero.core.item.MenuElement
import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.menu.MenuSession
import cc.trixey.invero.core.menu.PanelAgent
import cc.trixey.invero.core.util.castBoolean
import cc.trixey.invero.impl.element.MenuItemElement
import org.bukkit.inventory.meta.ItemMeta
import taboolib.platform.util.modifyMeta

/**
 * Invero
 * cc.trixey.invero.impl.IconStandard
 *
 * @author Arasple
 * @since 2023/1/15 13:59
 */
class IconStandard(
    override val condition: Condition?,
    override val handler: IconHandler,
    override val updateInterval: Int,
    override val updateProperties: Set<IconUpdatable>,
    override val relocateInterval: Int,
    override val slots: Set<Pos>?,
    override val subIcons: List<Icon>?,
    override val parent: Icon?,
    val texture: Texture,
    val name: String,
    val description: List<String>,
    val properties: Set<ItemProperty>,
) : Icon {

    override fun create(session: MenuSession, panelAgent: PanelAgent<*>, panel: ElementalPanel): MenuElement {
        val element = MenuItemElement(panelAgent, panel)

        // 首次渲染物品材质
        texture.generateItem(session).thenApply { item ->
            // apply properties
            properties.forEach { it.invoke(session, item) }
            // modify name, lore
            item.modifyMeta<ItemMeta> {
                setDisplayName(name)
                lore = description
            }
            // set item
            element.value = item
        }

        // 部署周期更新任务
        if (updateInterval > 0) {
            session.launchAsync(period = updateInterval.toLong()) {
                // texture
                if (IconUpdatable.TEXTURE in updateProperties) {
                    element.value = texture.generateItem(session).get()
                }
                // name, lore
                element.modify {
                    if (IconUpdatable.NAME in updateProperties) {
                        name = session.parse(this@IconStandard.name)
                    }
                    if (IconUpdatable.LORE in updateProperties) {
                        lore.clear()
                        lore += session.parse(description)
                    }
                }
                // properties
                if (IconUpdatable.PROPERTIES in updateProperties) {
                    val cloned = element.value.clone()
                    properties.forEach { itemProperty -> itemProperty.invoke(session, cloned) }
                    element.value = cloned
                }
            }
        }

        // 部署子图标定位任务
        // TODO
        if (relocateInterval > 0 && !subIcons.isNullOrEmpty()) {
            session.launchAsync(period = relocateInterval.toLong()) {
                val previousIndex = element.subIconIndex
                val relocatedIndex =
                    subIcons.indexOfFirst { it.condition?.eval(session.context)?.getNow(false).castBoolean(false) }

                if (previousIndex != relocatedIndex) {
                    element.subIconIndex = relocatedIndex
                }
            }
        }
        return element
    }


}