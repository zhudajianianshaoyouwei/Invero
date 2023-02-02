plugins {
    kotlin("plugin.serialization") version "1.7.21"
}

dependencies {
    compileTabooLib()
    compileCore(11701)

    compileModule("module-common")
    compileModule("module-core")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}