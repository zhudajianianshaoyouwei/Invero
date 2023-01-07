val taboolibVersion: String by project

dependencies {
    compileOnly(project(":project:common"))

    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11903:11903:mapped")
    compileOnly("ink.ptms.core:v11903:11903:universal")
    compileOnly("io.izzel.taboolib:platform-bukkit:$taboolibVersion")
    compileOnly("io.izzel.taboolib:common:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-nms:$taboolibVersion")
}