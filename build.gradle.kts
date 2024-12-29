plugins {
    id("java")
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.liquibase.gradle") version "2.1.1"
}

group = "org.berneick"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Database
    runtimeOnly("org.postgresql:postgresql")

    // Liquibase
    implementation("org.liquibase:liquibase-core")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation ("org.testcontainers:testcontainers")
    testImplementation ("org.testcontainers:postgresql")
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("docker.host", "unix:///var/run/docker.sock")
}

tasks.bootJar {
    archiveFileName.set("wallet-app.jar")
}
