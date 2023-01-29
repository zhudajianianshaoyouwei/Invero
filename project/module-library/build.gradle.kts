val taboolibVersion: String by project

dependencies {
    compileOnly("ink.ptms.core:v11701:11701:universal")

    implementation("net.kyori:adventure-api:4.12.0")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")
    implementation("net.kyori:adventure-text-serializer-bungeecord:4.2.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.2.0")
    implementation("net.kyori:adventure-platform-api:4.2.0")
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")

    compileOnly("io.izzel.taboolib:common:$taboolibVersion")
    compileOnly("io.izzel.taboolib:common-5:$taboolibVersion")
    compileOnly("io.izzel.taboolib:platform-bukkit:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-kether:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-nms:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-nms-util:$taboolibVersion")
}