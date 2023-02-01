package cc.trixey.invero.common.api

import cc.trixey.invero.common.Menu
import java.io.File

/**
 * Invero
 * cc.trixey.invero.common.api.SerializeResult
 *
 * @author Arasple
 * @since 2023/2/1 17:03
 */
class SerializeResult(
    val menu: Menu? = null,
    val file: File,
    val state: State,
    val throwable: Throwable? = null
) {

    enum class State {

        SUCCESS,

        FAILURE_FILE,

        FAILURE_MENU,

        FAILURE_DUPLICATED

    }

}