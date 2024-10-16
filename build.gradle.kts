plugins {
    java
    id("org.springframework.boot") version libs.versions.springBoot.get()
    id("io.spring.dependency-management") version libs.versions.springDep.get()
    id("test-report-aggregation")
    id("pmd")
    id("checkstyle")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


val checkstyleVersion: String = libs.versions.checkStyle.get()
allprojects {
    group = "$group"
    version = "$version"

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    apply(plugin = "checkstyle")
    apply(plugin = "pmd")
    checkstyle {
        checkstyle.toolVersion = checkstyleVersion
    }

}

// todo: pmd
//subprojects {
//    apply(plugin = "pmd")
//    pmd {
//        toolVersion ="7.0.0"
//        isConsoleOutput = true
//        isIgnoreFailures = false
//        rulesMinimumPriority = 5
//        ruleSets = listOf("category/java/errorprone.xml", "category/java/bestpractices.xml")
//    }
//}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}


dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${libs.versions.springBoot.get()}")  // Import Spring Boot BOM
    }
}


springBoot {
    mainClass.set("com.example.kitchensinkboot.app.KitchenSinkBootApplication")
}


dependencies {
    runtimeOnly(project(":app"))
}
