package cc.trixey.invero.core.geneartor

import cc.trixey.invero.common.supplier.Object
import cc.trixey.invero.common.supplier.sourceObject
import org.bukkit.Bukkit

/**
 * Invero
 * cc.trixey.invero.core.geneartor.Worlds
 *
 * @author Arasple
 * @since 2023/2/2 14:34
 */
class Worlds : BaseGenerator() {

    override fun generate(): List<Object> {
        generated = Bukkit.getWorlds().map {
            sourceObject {
                put("name", it.name)
                put("uid", it.uid)
                put("environment", it.environment.name)
                put("seed", it.seed)
                put("minHeight", it.minHeight)
                put("maxHeight", it.maxHeight)
                put("allowAnimals", it.allowAnimals)
                put("allowMonsters", it.allowMonsters)
                put("difficulty", it.difficulty)
                put("time", it.time)
            }
        }
        return generated!!
    }

}