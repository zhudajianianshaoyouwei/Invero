plugins {
    kotlin("plugin.serialization") version "1.7.21"
}

dependencies {
    compileTabooLib()
    compileCore(11903)
    framework()
    serialization()

    compileModule("module-common")
    compileModule("module-core")
}