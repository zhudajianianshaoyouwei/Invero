package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/22 20:50
 */
abstract class PanelInstance(
    override val parent: PanelContainer,
    override val weight: PanelWeight,
    override val scale: IScale,
    override val locate: Pos
) : Panel