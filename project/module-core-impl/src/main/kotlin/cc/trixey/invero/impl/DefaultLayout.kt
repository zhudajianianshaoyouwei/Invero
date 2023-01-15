package cc.trixey.invero.impl

import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Positions
import cc.trixey.invero.core.menu.Layout
import cc.trixey.invero.core.regex.LAYOUT_ICON_KEY

/**
 * Invero
 * cc.trixey.invero.impl.DefaultLayout
 *
 * @author Arasple
 * @since 2023/1/14 17:18
 */
class DefaultLayout(lines: List<String>) : Layout {

    private val mapped = mutableMapOf<String, Positions>().apply {
        lines.forEachIndexed { rows, line ->
            if (line.isNotBlank()) {
                split(line).forEachIndexed { index, key ->
                    if (key.isNotBlank()) {
                        computeIfAbsent(key) { Positions() } += Pos(index to rows)
                    }
                }
            }
        }
    }

    override fun search(key: String): Set<Pos> {
        return mapped[key]?.values ?: setOf()
    }

    companion object {

        private fun split(input: String): List<String> {
            val matcher = LAYOUT_ICON_KEY.matcher(input)
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