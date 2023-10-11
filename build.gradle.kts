import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.2.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
    // id("io.gitlab.arturbosch.detekt") version "1.23.1"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
}

group = "com.mtech.sj.bff"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

val springBootVersion = "3.1.0"
val springVersion = "6.0.12"
val springSecurityVersion = "6.1.4"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-autoconfigure:$springBootVersion")
    implementation("org.springframework.boot:spring-boot:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-logging:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-json:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-json:$springBootVersion")

    implementation("org.springframework:spring-core:$springVersion")
    implementation("org.springframework:spring-context:$springVersion")
    implementation("org.springframework:spring-beans:$springVersion")
    implementation("org.springframework:spring-webflux:$springVersion")
    implementation("org.springframework:spring-web:$springVersion")

    implementation("org.springframework.security:spring-security-web:$springSecurityVersion")
    implementation("org.springframework.security:spring-security-config:$springSecurityVersion")
    implementation("org.springframework.security:spring-security-crypto:$springSecurityVersion")
    implementation("org.springframework.security:spring-security-core:$springSecurityVersion")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("software.amazon.awscdk:cognito:1.204.0")
    implementation("software.amazon.awssdk:cognitoidentityprovider:2.20.149")

    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<BootJar>("bootJar") {
    archiveFileName = "sj_bff.jar"
    enabled = true
}
