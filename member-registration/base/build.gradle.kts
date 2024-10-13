plugins {
    `java-library`
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    api("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(platform(libs.joinfaces.platform))
    implementation(libs.jakarta.faces.api)
    implementation(libs.jakarta.faces)
    runtimeOnly("org.joinfaces:faces-spring-boot-starter")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    annotationProcessor("org.projectlombok:lombok")
}