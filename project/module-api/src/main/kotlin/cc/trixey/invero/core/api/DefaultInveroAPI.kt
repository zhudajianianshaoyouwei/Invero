package cc.trixey.invero.core.api

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.api.DataManager
import cc.trixey.invero.common.api.InveroAPI
import cc.trixey.invero.common.api.JavaScriptHandler
import cc.trixey.invero.common.api.MenuManager
import cc.trixey.invero.common.supplier.ElementGenerator
import cc.trixey.invero.common.supplier.ItemSourceProvider
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.platform.util.bukkitPlugin
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.api.DefaultInveroAPI
 *
 * @author Arasple
 * @since 2023/2/1 17:16
 */
class DefaultInveroAPI : InveroAPI {

    private val itemSourceProvider = ConcurrentHashMap<String, ItemSourceProvider>()
    private val elementGenerators = ConcurrentHashMap<String, ElementGenerator>()

    override fun getMenuManager() = PlatformFactory.getAPI<MenuManager>()

    override fun getDataManager() = PlatformFactory.getAPI<DataManager>()

    override fun getJavaScriptHandler() = PlatformFactory.getAPI<JavaScriptHandler>()

    override fun registerElementGenerator(namespace: String, id: String, provider: ElementGenerator) {
        val identifier = if (namespace == bukkitPlugin.name) id else "$namespace:$id"
        elementGenerators[identifier] = provider
    }

    override fun createElementGenerator(identifier: String): ElementGenerator? =
        elementGenerators.entries
            .find {
                it.key.equals(identifier, true) || it.key.removeSuffix("s").equals(identifier, true)
            }
            ?.value
            ?.javaClass
            ?.getConstructor()
            ?.newInstance()

    override fun registerItemSourceProvider(name: String, provider: ItemSourceProvider) {
        itemSourceProvider[name.uppercase()] = provider
    }

    override fun getItemSourceProvider(name: String): ItemSourceProvider? {
        return itemSourceProvider[name.uppercase()]
    }


    companion object {

        @Awake(LifeCycle.INIT)
        fun init() {
            PlatformFactory.registerAPI<InveroAPI>(DefaultInveroAPI())
        }

    }

}