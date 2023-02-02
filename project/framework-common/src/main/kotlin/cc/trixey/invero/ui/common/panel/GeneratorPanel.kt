package cc.trixey.invero.ui.common.panel

import cc.trixey.invero.ui.common.Pos

/**
 * Invero
 * cc.trixey.invero.ui.common.panel.GeneratorPanel
 *
 * @author Arasple
 * @since 2023/1/11 15:56
 */
interface GeneratorPanel<T, R : Any> : ElementalPanel {

    var sourceElements: List<T>

    val outputElements: ArrayList<R?>

    var outputGenerator: (T) -> R?

    val generatorPool: List<Pos>

    fun getOutput(index: Int): R? {
        if (outputElements[index] == null) {
            outputElements[index] = outputGenerator(sourceElements[index])
        }
        return outputElements[index]
    }

    fun reset()

    fun filter(block: (T) -> Boolean) {
        sourceElements = sourceElements.filter(block)
        reset()
    }

    fun <R : Comparable<R>> sortBy(block: (T) -> R) {
        sourceElements = sourceElements.sortedBy(block)
        reset()
    }

    fun generatorElements(block: () -> List<T>) {
        sourceElements = block()
        reset()
    }

    fun onGenerate(block: (T) -> R?) {
        outputGenerator = block
    }

}