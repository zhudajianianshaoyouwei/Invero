package cc.trixey.invero.core

import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Positions
import cc.trixey.invero.core.util.SPECIAL_GROUP
import cc.trixey.invero.core.serialize.LayoutSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Invero
 * cc.trixey.invero.core.Layout
 *
 * @author Arasple
 * @since 2023/1/15 22:35
 */
@Serializable(with = LayoutSerializer::class)
class Layout(val raw: List<String>) {

    @Transient
    private val scale = raw.let { list ->
        list.maxBy { it.length }.length to list.size
    }

    @Transient
    private val mapped = mutableMapOf<String, Positions>().apply {
        raw.forEachIndexed { rows, line ->
            if (line.isNotBlank()) {
                split(line).forEachIndexed { index, key ->
                    if (key.isNotBlank()) {
                        computeIfAbsent(key) { Positions() } += Pos(index to rows)
                    }
                }
            }
        }
    }

    @Transient
    private val coerecedScale = mapped.values.flatMap { it.values }.let { posList ->
        posList.maxBy { it.x }.x + 1 to posList.maxBy { it.y }.y + 1
    }

    fun getScale(): Pair<Int, Int> {
        return scale
    }

    fun getCoerecedScale(): Pair<Int, Int> {
        mapped.maxBy { it.value.values.maxBy { it.y } }
        return scale
    }

    fun search(key: String?): Set<Pos> {
        return mapped[key]?.values ?: setOf()
    }

    companion object {

        private fun split(input: String): List<String> {
            val matcher = SPECIAL_GROUP.matcher(input)
            if (!matcher.find()) return input.map { it.toString() }

            val result = mutableListOf<String>()
            var lastStart = 0

            do {
                result += input.substring(lastStart, matcher.start()).map { it.toString() }
                result += matcher.group(1)
                lastStart = matcher.end()
            } while (matcher.find())

            result += input.substring(lastStart, input.length).map { it.toString() }

            return result
        }

    }

}