plugins {
    kotlin("plugin.serialization") version "1.7.21"
}

dependencies {
    compileTabooLib()
    compileNMS()
    compileCore(11903)
    framework()

    compileModule("module-common")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}