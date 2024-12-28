import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


val cucumberVersion: String by extra { "6.11.0" }
val kotestVersion: String by extra { "5.5.5" }
val telegramStarterVersion: String by extra { "6.5.0" }
val testcontainersBomVersion: String by extra { "1.17.4" }

plugins {
    val kotlinVersion = "1.7.10"

    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("nebula.ospackage") version "9.1.1"
}

group = "com.notificator.bot"
version = "0.1.6"

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
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    // actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // db
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    // kotlin logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    // telegram starter
    implementation("org.telegram:telegrambots-spring-boot-starter:$telegramStarterVersion")
    // cache
    implementation("com.github.ben-manes.caffeine:caffeine")
    // devtools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    // testcontainers
    testImplementation(platform("org.testcontainers:testcontainers-bom:$testcontainersBomVersion"))
    testImplementation("org.testcontainers:postgresql")
    // cucumber
    testImplementation("io.cucumber:cucumber-java8:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-spring:$cucumberVersion")

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
