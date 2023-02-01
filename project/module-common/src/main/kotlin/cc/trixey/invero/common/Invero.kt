package cc.trixey.invero.common

import cc.trixey.invero.common.api.InveroAPI
import taboolib.common.platform.PlatformFactory

/**
 * Invero
 * cc.trixey.invero.common.Invero
 *
 * @author Arasple
 * @since 2023/2/1 17:13
 */
object Invero {

    private var api: InveroAPI? = null

    fun api(): InveroAPI {
        if (api == null) api = PlatformFactory.getAPI()
        return api ?: error("InveroAPI is not initialized")
    }

}