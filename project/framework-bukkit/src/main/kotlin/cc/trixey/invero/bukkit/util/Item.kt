package cc.trixey.invero.bukkit.util

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir

fun ItemStack.mark(viewer: String, slot: Int): ItemStack {
    if (isNotAir()) {
//        ItemTag().apply {
//            putAll(getItemTag())
//            put("invero", ItemTagData.toNBT(buildMap {
//                put("viewer", viewer)
//                put("slot", slot)
//            }))
//        }.saveTo(this)
    }
    return this
}

fun randomItem(builder: ItemBuilder.() -> Unit = {}): ItemStack {
    var itemStack: ItemStack? = null
    while (itemStack.isAir()) {
        itemStack = ItemStack(Material.values().filter { it.isItem || it.isBlock }.random())
    }
    return buildItem(itemStack!!, builder).also {
        it.amount = it.amount.coerceAtMost(it.maxStackSize)
    }
}

fun randomMaterial(): Material {
    return Material.values().filter { (it.isItem || it.isBlock) && it.isNotAir() }.random()
}