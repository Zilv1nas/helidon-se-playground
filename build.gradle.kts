import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
}

group = "com.github.zilv1nas"
version = "1.0-SNAPSHOT"

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("io.helidon:helidon-dependencies:3.1.1"))
    implementation("io.helidon.config:helidon-config-yaml")

    implementation("io.helidon.messaging:helidon-messaging")
    implementation("io.helidon.messaging.kafka:helidon-messaging-kafka")

    implementation("io.helidon.webserver:helidon-webserver")
    implementation("io.helidon.webserver:helidon-webserver-access-log")
    implementation("io.helidon.media:helidon-media-jackson")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("io.helidon.health:helidon-health-checks")

    implementation("io.helidon.dbclient:helidon-dbclient")
    implementation("io.helidon.dbclient:helidon-dbclient-jdbc")
    implementation("io.helidon.dbclient:helidon-dbclient-health")
    implementation("org.postgresql:postgresql:42.5.3")
    implementation("org.flywaydb:flyway-core:9.14.1")

    implementation("io.insert-koin:koin-core:3.3.3")
    implementation("org.slf4j:slf4j-api")
    implementation("io.helidon.logging:helidon-logging-slf4j")
    implementation("org.slf4j:jul-to-slf4j")
    implementation("ch.qos.logback:logback-classic")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}