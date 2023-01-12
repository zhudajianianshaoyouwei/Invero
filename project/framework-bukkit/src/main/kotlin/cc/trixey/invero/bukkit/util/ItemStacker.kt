package cc.trixey.invero.bukkit.util

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import taboolib.platform.util.isAir

/**
 * Invero
 * cc.trixey.invero.bukkit.util.ItemStacker
 *
 * @author Arasple
 * @since 2023/1/12 13:57
 */
fun ItemStack.reachedMaxStackSize(): Boolean {
    return amount >= maxStackSize
}

//fun PlayerInventory.stackItemStack(input: ItemStack) {
//    storageContents.forEach {
//        while (it != null && input.amount > 0 && it.isSimilar(input) && !it.reachedMaxStackSize()) {
//            it.amount++
//            input.amount--
//        }
//    }
//
//    val index = storageContents.indexOfFirst { it.isAir }
//
//    if (input.amount > 0 && index >= 0) {
//        storageContents[index] = input.clone()
//        input.amount = 0
//    } else {
//        println("INDEX: $index")
//    }
//}