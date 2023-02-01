plugins {
    id("io.izzel.taboolib") version taboolibPluginVersion
}

taboolib {
    install("common")
    install("common-5")
    install("module-nms")
    install("module-nms-util")
    install("module-kether")
    install("module-configuration")
    install("module-lang")
    install("module-chat")
    install("module-database")
    install("expansion-javascript")
    install("expansion-player-database")
    install("platform-bukkit")

    description {
        name = projectName

        contributors {
            name("Arasple")
        }

        dependencies {
            name("PlaceholderAPI").optional(true)
            name("Zaphkiel").optional(true)
            name("HeadDatabase").optional(true)
            name("Oraxen").optional(true)
            name("ItemsAdder").optional(true)
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
    archiveBaseName.set(projectName)
}