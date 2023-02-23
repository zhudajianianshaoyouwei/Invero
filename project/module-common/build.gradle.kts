dependencies {
    compileTabooLib()
    compileCore(11604, complete = true)
    framework()
    serialization()

    compileOnly("net.kyori:adventure-api:4.12.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
    compileOnly("net.kyori:adventure-text-serializer-bungeecord:4.2.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.2.0")
    compileOnly("net.kyori:adventure-platform-api:4.2.0")
    compileOnly("net.kyori:adventure-platform-bukkit:4.2.0")
}