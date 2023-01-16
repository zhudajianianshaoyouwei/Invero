package cc.trixey.invero.serialize

import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.item.TextureHead
import cc.trixey.invero.core.item.TextureMaterial
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

/**
 * Invero
 * cc.trixey.invero.serialize.SelectorTexture
 *
 * @author Arasple
 * @since 2023/1/16 10:31
 */
object SelectorTexture : JsonContentPolymorphicSerializer<Texture>(Texture::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Texture> {
        return if (element is JsonObject) {
            when {
                "head" in element.jsonObject -> TextureHead.serializer()
                "source" in element.jsonObject -> TextureHead.serializer()
                else -> error("Unregonized texture format: $element")
            }
        } else {
            TextureMaterial.serializer()
        }
    }

}