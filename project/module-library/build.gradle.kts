val taboolibVersion: String by project

dependencies {
    compileOnly("ink.ptms.core:v11903:11903-minimize:universal")
    compileOnly("io.izzel.taboolib:common:$taboolibVersion")
    compileOnly("io.izzel.taboolib:common-5:$taboolibVersion")
    compileOnly("io.izzel.taboolib:platform-bukkit:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-kether:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-nms:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-nms-util:$taboolibVersion")
}