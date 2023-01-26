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

@OptIn(ExperimentalSerializationApi::class)
private val module = SerializersModule {

    polymorphic(AgentPanel::class) {
        subclass(PanelStandard::class)
    }

    polymorphic(Action::class) {
        subclass(ActionKether::class)
        subclass(StructureActionIf::class)
        subclass(StructureActionIfNot::class)
        subclass(StructureActionWhen::class)
        subclass(StructureActionKether::class)
        subclass(NetesedAction::class)
    }

}

val mainJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    serializersModule = module
    explicitNulls = false
}

fun Configuration.deserializeToMenu(name: String? = null): Menu {
    changeType(Type.JSON)
    return mainJson.decodeFromString<Menu>(saveToString()).also { if (name != null && it.name == null) it.name = name }
}

fun Menu.serializeToJson(): String {
    return mainJson.encodeToString(value = this)
}