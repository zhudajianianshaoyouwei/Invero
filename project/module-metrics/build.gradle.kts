dependencies {
    compileTabooLib()
    compileCore(11903)
    framework()

    compileModule("module-common")
    compileModule("module-core")

    implementation("org.bstats:bstats-bukkit:3.0.0")
}