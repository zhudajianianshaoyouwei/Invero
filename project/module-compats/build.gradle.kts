repositories {
    maven("https://jitpack.io")
    maven("https://repo.rosewooddev.io/repository/public/")
}

dependencies {
    compileTabooLib()
    compileCore(11903)
    serialization()
    framework()
    compileLocal(project, "libs")

    compileModule("module-common")
    compileModule("module-core")

    compileOnly("com.github.oraxen:oraxen:-SNAPSHOT")
    compileOnly("ink.ptms:Zaphkiel:1.6.0")
    compileOnly("org.black_ixx:playerpoints:3.1.1")
}