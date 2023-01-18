@file:OptIn(ExperimentalSerializationApi::class)
@file:RuntimeDependencies(
    RuntimeDependency(
        "!org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.4.1",
        test = "!kotlinx.serialization.Serializer",
        relocate = ["!kotlin.", "!kotlin@kotlin_version_escape@.", "!kotlinx.", "!kotlinx_1_4_1."],
        transitive = false
    ),
    RuntimeDependency(
        "!org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.4.1",
        test = "!kotlinx.serialization.json.Json",
        relocate = ["!kotlin.", "!kotlin@kotlin_version_escape@.", "!kotlinx.", "!kotlinx_1_4_1."],
        transitive = false
    )
)

package cc.trixey.invero.core.serialize

import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Menu
import cc.trixey.invero.core.action.*
import cc.trixey.invero.core.panel.PanelStandard
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type

/**
 * Invero
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
        subclass(StructureActionWhen::class)
        subclass(NetesedAction::class)
    }

}

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