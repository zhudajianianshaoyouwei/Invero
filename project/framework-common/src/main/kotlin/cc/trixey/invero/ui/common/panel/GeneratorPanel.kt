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

    var generated: List<T>

    var currentSource: List<T>

    val outputElements: ArrayList<R?>

    var outputGenerator: (T) -> R?

    val generatorPool: List<Pos>

    fun getOutput(index: Int): R? {
        if (outputElements[index] == null) {
            outputElements[index] = outputGenerator(currentSource[index])
        }
        return outputElements[index]
    }

    fun reset()

    fun filter(block: (T) -> Boolean) {
        currentSource = generated.filter(block)
        reset()
    }

    fun <R : Comparable<R>> sortBy(block: (T) -> R) {
        currentSource = generated.sortedBy(block)
        reset()
    }

    fun generatorElements(block: () -> List<T>) {
        generated = block()
        currentSource = generated
        reset()
    }

    fun onGenerate(block: (T) -> R?) {
        outputGenerator = block
    }

}