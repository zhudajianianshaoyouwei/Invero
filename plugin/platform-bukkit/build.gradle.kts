val taboolibVersion: String by project

plugins {
    id("io.izzel.taboolib") version ("1.53")
}

taboolib {
    install("common")
    install("module-nms")
    install("module-configuration")
    install("platform-bukkit")

    description {
        name = rootProject.name

        contributors {
            name("Arasple")
        }
    }

    classifier = null
    version = taboolibVersion
}

dependencies {
    compileOnly("ink.ptms.core:v11903:11903-minimize:universal")

    rootProject
        .childProjects["project"]!!
        .childProjects
        .forEach {
            taboo(it.value)
        }
}

tasks.jar {
    archiveBaseName.set(rootProject.name)
}