val taboolibVersion: String by project

plugins {
    kotlin("plugin.serialization") version "1.7.21"
}

dependencies {
    compileOnly(project(":project:framework-common"))
    compileOnly(project(":project:framework-bukkit"))
    compileOnly(project(":project:module-library"))

    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    compileOnly("net.kyori:adventure-api:4.12.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.12.0")

    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11903:11903-minimize:universal")

    compileOnly("io.izzel.taboolib:common:$taboolibVersion")
    compileOnly("io.izzel.taboolib:common-5:$taboolibVersion")
    compileOnly("io.izzel.taboolib:platform-bukkit:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-configuration:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-kether:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-nms:$taboolibVersion")
}