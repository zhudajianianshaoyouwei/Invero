val taboolibVersion: String by project

dependencies {
    compileOnly("ink.ptms.core:v11903:11903-minimize:universal")
    compileOnly("io.izzel.taboolib:platform-bukkit:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-configuration:$taboolibVersion")
}