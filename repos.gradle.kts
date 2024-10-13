dependencyResolutionManagement {

    repositories {
        mavenLocal()
        maven {
            name = "maven-public"
            isAllowInsecureProtocol = true
            url = uri(extra["ARTIFACT_REPO"].toString() + "/maven-public")
            credentials {
                username = extra["ARTIFACT_USER"].toString()
                password = extra["ARTIFACT_PASS"].toString()
            }
        }
        maven {
            name = "maven-plugins"
            isAllowInsecureProtocol = true
            url = uri(extra["ARTIFACT_REPO"].toString() + "/gradle-plugins")
            credentials {
                username = extra["ARTIFACT_USER"].toString()
                password = extra["ARTIFACT_PASS"].toString()
            }
        }
    }
}

pluginManagement {
    repositories {
        maven {
            name = "maven-public"
            isAllowInsecureProtocol = true
            url = uri(extra["ARTIFACT_REPO"].toString() + "/maven-public")
            credentials {
                username = extra["ARTIFACT_USER"].toString()
                password = extra["ARTIFACT_PASS"].toString()
            }
        }
        maven {
            name = "maven-plugins"
            isAllowInsecureProtocol = true
            url = uri(extra["ARTIFACT_REPO"].toString() + "/gradle-plugins")
            credentials {
                username = extra["ARTIFACT_USER"].toString()
                password = extra["ARTIFACT_PASS"].toString()
            }
        }
    }
}
