package cc.trixey.invero.core.api

import cc.trixey.invero.common.ElementGenerator
import cc.trixey.invero.common.ItemSourceProvider
import cc.trixey.invero.common.api.*
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

    override fun getMenuManager() = PlatformFactory.getAPI<InveroMenuManager>()

    override fun getDataManager() = PlatformFactory.getAPI<InveroDataManager>()

    override fun getJavaScriptHandler() = PlatformFactory.getAPI<InveroJavaScriptHandler>()

    override fun getKetherHandler() = PlatformFactory.getAPI<InveroKetherHandler>()

    override fun getRegistry() = PlatformFactory.getAPI<InveroRegistry>()

    companion object {

        @Awake(LifeCycle.INIT)
        fun init() {
            PlatformFactory.registerAPI<InveroAPI>(DefaultInveroAPI())
        }

    }

}