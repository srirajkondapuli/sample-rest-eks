pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
    }
}

buildCache {
    local {
        directory = File(rootDir, "build/cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "sample-rest-eks"
