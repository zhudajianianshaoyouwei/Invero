package cc.trixey.invero.core.icon

import cc.trixey.invero.core.texture.Texture

/**
 * Invero
 * cc.trixey.invero.core.icon.IconDisplay
 *
 * @author Arasple
 * @since 2023/1/14 14:07
 */
interface IconDisplay {

    fun getTexture(): Texture

    fun getName(): String

    fun getLore(): List<String>

}