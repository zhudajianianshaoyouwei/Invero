@file:OptIn(ExperimentalSerializationApi::class, ExperimentalSerializationApi::class)

package cc.trixey.invero.core

import cc.trixey.invero.core.action.*
import cc.trixey.invero.core.panel.PanelStandard
import cc.trixey.invero.core.serialize.hocon.HoconLoader
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.io.File

/**
 * Invero_Core
 * cc.trixey.invero.Main
 *
 * @author Arasple
 * @since 2023/1/17 22:50
 */
private val module = SerializersModule {
    polymorphic(AgentPanel::class) {
        subclass(PanelStandard::class)
    }

    polymorphic(Action::class) {
        subclass(ActionKether::class)
        subclass(StructureActionTertiary::class)
    }
}

@OptIn(ExperimentalSerializationApi::class)
private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    serializersModule = module
    explicitNulls = false
}

fun Configuration.toMenu(): Menu {
    changeType(Type.JSON)
    return json.decodeFromString(saveToString())
}

fun Menu.toJson(): String {
    return json.encodeToString(value = this)
}

fun testMenu() {
    val file = File("F:\\Projects\\Invero_Core\\src\\main\\resources\\example.conf")
    val conf = HoconLoader.loadFromFile(file, destinationType = Type.JSON)

    val decoded = conf.toMenu()
    val encoded = decoded.toJson()

    println("Successed.")

    decoded.panels.flatMap { (it as PanelStandard).icons.values }.forEach {
        println(json.encodeToString(it))
    }
}

fun testAction() {
    val ketherActions = arrayOf(
        JsonPrimitive("print just a String"),
        buildJsonArray {
            add("print hello world")
            add("tell colored message")
        }
    ).map { json.decodeFromJsonElement<ActionKether>(it) }


    val tertiary = StructureActionTertiary(
        ScriptKether("perm op"),
        StructureActionWhen(
            ScriptKether("player name"),
            mapOf(
                Comparator.parse(">= 200") to ketherActions[0]
            )
        ),
        ketherActions[1]
    )

    println(ketherActions)
    println(ketherActions.map { json.encodeToString(it) })

    val encoded = json.encodeToString(tertiary)
    val decoded = json.decodeFromString<Action>(encoded)

    println(encoded)
    println("--------------------")
    println(json.encodeToString(decoded))
}

fun main() {
    testMenu()
}