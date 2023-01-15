package cc.trixey.invero.core.serialize.ref

import cc.trixey.invero.core.serialize.util.tolerantRegex

/**
 * Invero
 * cc.trixey.invero.core.serialize.ref.Nodes
 *
 * @author Arasple
 * @since 2023/1/15 14:28
 */
val NODE_MENU = tolerantRegex("menus?")

val NODE_MENU_TYPE = tolerantRegex("types?")

val NODE_MENU_ROWS = tolerantRegex("rows?")

val NODE_MENU_TITLE = tolerantRegex("title?")