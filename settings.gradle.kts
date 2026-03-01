pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AllAiNovel"
include(":app")
include(":core-ui")
include(":core-common")
include(":core-datastore")
include(":core-network")
include(":core-database")
include(":domain")
include(":feature-book")
include(":feature-settings")
include(":feature-editor")
include(":feature-chapter")
include(":feature-ai")
include(":feature-outline")
include(":feature-character")
include(":feature-worldbuilding")
include(":feature-statistics")
include(":feature-tools")
include(":feature-oneclick")
