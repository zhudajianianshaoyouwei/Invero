val taboolibVersion: String by project

dependencies {
    compileOnly(project(":project:framework-common"))
    compileOnly(project(":project:framework-bukkit"))
    compileOnly(project(":project:module-core"))
    compileOnly(project(":project:module-library"))
    compileOnly(project(":project:module-compats"))

    compileOnly("ink.ptms.core:v11903:11903-minimize:universal")
    compileOnly("com.mojang:datafixerupper:4.0.26")

    compileOnly("io.izzel.taboolib:common:$taboolibVersion")
    compileOnly("io.izzel.taboolib:common-5:$taboolibVersion")
    compileOnly("io.izzel.taboolib:platform-bukkit:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-kether:$taboolibVersion")
}