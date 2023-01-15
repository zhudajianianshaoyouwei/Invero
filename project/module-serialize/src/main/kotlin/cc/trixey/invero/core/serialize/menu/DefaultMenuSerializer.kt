package cc.trixey.invero.core.serialize.menu

import cc.trixey.invero.common.ContainerType
import cc.trixey.invero.common.StorageMode
import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.menu.PanelAgent
import cc.trixey.invero.core.serialize.MenuSerializer
import cc.trixey.invero.core.serialize.TolorantType.*
import cc.trixey.invero.core.serialize.util.castList
import cc.trixey.invero.core.serialize.util.castSection
import cc.trixey.invero.core.serialize.util.getTolerant
import cc.trixey.invero.core.serialize.util.getTolerantKey
import cc.trixey.invero.impl.DefaultMenu
import cc.trixey.invero.impl.DefaultMenuOptions
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration

/**
 * Invero
 * cc.trixey.invero.core.serialize.menu.DefaultMenuSerializer
 *
 * @author Arasple
 * @since 2023/1/14 21:30
 */
object DefaultMenuSerializer : MenuSerializer<DefaultMenu> {

    override fun load(conf: Configuration): DefaultMenu {
        val name = conf.file?.nameWithoutExtension ?: "Unnamed"
        val title = mutableListOf<String>()
        var titleFramePeriod: Int = -1
        var titleFrameMode: CycleMode = CycleMode.LOOP
        var containerType: ContainerType = ContainerType.GENERIC_9X6
        var rows: Int = 6
        var panels = mutableListOf<PanelAgent<*>>()

        // title
        conf
            .getTolerant("titles?", STRING, SECTION)
            ?.let {
                if (it.first == STRING) title += it.second.toString()
                else it.second.castSection().apply {
                    title += getStringList("value")
                    titleFramePeriod = getInt("period")
                    titleFrameMode = getEnum("mode", CycleMode::class.java) ?: CycleMode.LOOP
                }
            }

        // container type
        conf
            .getTolerantKey("types?")
            ?.let {
                containerType = conf.getEnum(it, ContainerType::class.java) ?: ContainerType
                    .fromBukkitType(conf.getString(it, "chest")!!)
            }

        // rows
        conf
            .getTolerantKey("rows?|lines?")
            ?.let {
                rows = conf.getInt(it, 6)
            }

        // TODO storageMode
        val storageMode = StorageMode()
        val options = DefaultMenuOptions()

        // panels
        conf
            .getTolerant("panels?", LIST_OBJECT, SECTION)
            ?.let { tolorantTypeAnyPair ->
                val (type, value) = tolorantTypeAnyPair
                if (type == SECTION) {
                    panels += loadPanel(value.castSection())
                } else {
                    panels += value.castList().map { loadPanel(it.castSection()) }
                }
            }

        return DefaultMenu(
            name,
            title.toTypedArray(),
            titleFramePeriod,
            titleFrameMode,
            containerType,
            storageMode,
            options,
            panels
        )
    }

    override fun loadPanel(section: ConfigurationSection): PanelAgent<*> {
        TODO("Not yet implemented")
    }

}