package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/22 20:50
 */
abstract class PanelInstance(
    override val parent: PanelContainer,
    override val weight: PanelWeight,
    override val scale: Pair<Int, Int>,
    override val locate: Pos
) : Panel {

    val size by lazy { scale.first * scale.second }

}