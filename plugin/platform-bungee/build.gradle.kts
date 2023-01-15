val taboolibVersion: String by project

plugins {
    id("io.izzel.taboolib") version ("1.53")
}

taboolib {
    install("common")
    install("platform-bungee")

    description {
        name = rootProject.name

        contributors {
            name("Arasple")
        }
    }

    classifier = null
    version = taboolibVersion
}

tasks.jar {
    archiveBaseName.set(rootProject.name)
}