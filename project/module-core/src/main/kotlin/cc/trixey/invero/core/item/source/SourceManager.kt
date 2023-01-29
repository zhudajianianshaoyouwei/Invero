package cc.trixey.invero.core.item.source

import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.item.source.SourceManager
 *
 * @author Arasple
 * @since 2023/1/29 15:35
 */
object SourceManager {

    private val sourceProviders = ConcurrentHashMap<String, Provider>()

    fun getProvider(name: String): Provider? {
        return sourceProviders.getOrElse(name) {
            sourceProviders.entries.find { it.key.equals(name, true) }?.value
        }
    }

    fun register(name: String, provider: Provider) {
        sourceProviders[name] = provider
    }


}