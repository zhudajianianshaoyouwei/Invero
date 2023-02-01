plugins {
    id("io.izzel.taboolib") version taboolibPluginVersion
}

taboolib {
    install("common")
    install("platform-bungee")

    description {
        name = projectName

        contributors {
            name("Arasple")
        }
    }

    classifier = null
    version = taboolibVersion
}

tasks.jar {
    archiveBaseName.set(projectName)
}