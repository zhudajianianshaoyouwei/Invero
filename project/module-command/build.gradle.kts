val taboolibVersion: String by project

dependencies {
    compileOnly(project(":project:framework-common"))
    compileOnly(project(":project:framework-bukkit"))
    compileOnly(project(":project:module-core"))

    compileOnly("ink.ptms.core:v11903:11903-minimize:universal")

    compileOnly("io.izzel.taboolib:common:$taboolibVersion")
    compileOnly("io.izzel.taboolib:platform-bukkit:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-chat:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-lang:$taboolibVersion")
    compileOnly("io.izzel.taboolib:expansion-player-database:$taboolibVersion")
}