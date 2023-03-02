package cc.trixey.invero.common.adventure

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info

/**
 * Invero
 * cc.trixey.invero.common.adventure.Adventure 🤡
 *
 * @author Arasple
 * @since 2023/2/26 12:58
 */
object Adventure {

    private var PLATFORM: AdventurePlatform? = null

    val isSupported: Boolean
        get() = PLATFORM != null

    fun parse(content: String): String {
        return PLATFORM?.parseMiniMessage(content) ?: content
    }

    @Awake(LifeCycle.LOAD)
    internal fun init() {
        runCatching {
            PLATFORM = AdventurePlatform()
            PLATFORM?.parseMiniMessage("<red>adventure = 🤡")
        }.onSuccess {
            info("Successfully loaded Adventure support.")
        }.onFailure {
            PLATFORM = null
            info("Not find Adventure support.")
        }
    }

}