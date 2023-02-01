@file:OptIn(ExperimentalSerializationApi::class, ExperimentalSerializationApi::class)
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

import kotlinx.serialization.ExperimentalSerializationApi
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency

/**
 * Invero
 * cc.trixey.invero.Main
 *
 * @author Arasple
 * @since 2023/1/17 22:50
 */

