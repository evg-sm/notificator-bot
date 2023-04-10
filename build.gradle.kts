import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    val kotlinVersion = "1.6.10"

    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
}

group = "com.notificator.bot"
version = "0.0.3"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    // spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // db
    implementation("com.h2database:h2")
    implementation("org.flywaydb:flyway-core")
    // kotlin logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    // telegram
    implementation("org.telegram:telegrambots-spring-boot-starter:6.5.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}

springBoot {
    buildInfo()
}
