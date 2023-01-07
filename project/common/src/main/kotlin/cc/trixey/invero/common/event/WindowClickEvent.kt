package cc.trixey.invero.common.event

/**
 * @author Arasple
 * @since 2023/1/6 11:47
 */
interface WindowClickEvent : WindowEvent, Cancellable {

    val rawSlot: Int

    val clickType: ClickType

}