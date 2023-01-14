val taboolibVersion: String by project

dependencies {
    compileOnly(project(":project:module-core"))
    compileOnly(project(":project:module-core-impl"))
    compileOnly("io.izzel.taboolib:common:$taboolibVersion")
    compileOnly("io.izzel.taboolib:module-configuration:$taboolibVersion")
}