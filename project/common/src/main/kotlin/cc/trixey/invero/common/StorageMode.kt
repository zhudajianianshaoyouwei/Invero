package cc.trixey.invero.common

/**
 * Invero
 * cc.trixey.invero.common.StorageMode
 *
 * @author Arasple
 * @since 2023/1/11 21:47
 *
 * USE (always restore)
 *  - CLEAN & USE
 *  - NOT CLEAN & USE
 *
 * NOT USE (not restore, no exceed support, not clean)
 *
 */
data class StorageMode(val overridePlayerInventory: Boolean = true, val alwaysClean: Boolean = true) {

    val shouldBackup: Boolean
        get() = shouldRestore

    val shouldRestore: Boolean
        get() = overridePlayerInventory

    val shouldClean: Boolean
        get() = overridePlayerInventory && alwaysClean

}