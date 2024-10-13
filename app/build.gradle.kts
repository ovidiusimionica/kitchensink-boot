plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    runtimeOnly(project(":member-registration:base"))
    runtimeOnly(project(":member-registration:mongo"))
    testImplementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")
    testImplementation(project(":member-registration:base"))
    testRuntimeOnly(project(":member-registration:mongo"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

