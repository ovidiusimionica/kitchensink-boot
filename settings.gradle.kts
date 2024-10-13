rootProject.name = "kitchensink-boot"


val artifactRepo: String? = System.getenv("ARTIFACT_REPO")
val artifactUser: String? = System.getenv("ARTIFACT_USER")
val artifactPass: String? = System.getenv("ARTIFACT_PASS")

artifactRepo?.let { extra.set("ARTIFACT_REPO", it) }
artifactUser?.let { extra.set("ARTIFACT_USER", it) }
artifactPass?.let { extra.set("ARTIFACT_PASS", it) }


include("member-registration:base")
include("member-registration:mongo")
include("app")


artifactRepo?.let {
    apply(from = "$rootDir/repos.gradle.kts")
}

if (artifactRepo == null) {
    dependencyResolutionManagement {
        repositories {
            gradlePluginPortal()
            mavenCentral()
        }
    }

    pluginManagement {
        repositories {
            gradlePluginPortal()
            mavenCentral()
        }
    }
}


