val taboolibVersion: String by project

plugins {
    id("io.izzel.taboolib") version ("1.53")
}

taboolib {
    install("common")
    install("common-5")
    install("module-nms")
    install("module-kether")
    install("module-configuration")
    install("module-chat")
    install("platform-bukkit")

    description {
        name = rootProject.name

        contributors {
            name("Arasple")
        }

        dependencies {
            name("PlaceholderAPI")
        }
    }

    relocate("kotlinx.serialization", "kotlinx_1_4_1.serialization")
    relocate("kotlinx.coroutines", "kotlinx_1_6_4.coroutines")

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