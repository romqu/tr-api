import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.0-RC"
    kotlin("plugin.spring") version "1.5.0-RC"
    id("nu.studer.jooq") version "5.2"
    id("org.flywaydb.flyway") version "7.8.2"
}

group = "de.romqu"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-RC")

    implementation("org.jooq:jooq-codegen:3.14.9")
    jooqGenerator("org.postgresql:postgresql:42.2.19")
    jooqGenerator(project(":entity_strategy"))

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")

    implementation("com.auth0:java-jwt:3.15.0")
    implementation("org.bouncycastle:bcprov-jdk15on:1.68")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}



tasks.withType<Test> {
    useJUnitPlatform()
}

flyway {
    driver = "org.postgresql.Driver"
    url = "jdbc:postgresql://localhost/traderepublic?useSSL=false"
    user = "traderepublic_user"
    password = "1234"
}

jooq {
    version.set("3.14.7")

    configurations {
        create("main") {
            jooqConfiguration.apply {
                generator.apply {
                    database.apply {
                        includes = ".*"
                        excludes = "(?i:information_schema\\..*)|(?i:pg_catalog\\..*)|(flyway.*)"
                        isIncludeIndexes = false
                        isIncludeSequences = false
                    }
                    jdbc.apply {
                        driver = "org.postgresql.Driver"
                        url = "jdbc:postgresql://localhost/traderepublic?useSSL=false"
                        user = "traderepublic_user"
                        password = "1234"
                    }
                    name = "org.jooq.codegen.JavaGenerator"
                    generate.apply {
                        isEmptyCatalogs = true
                        isDeprecated = false
                        isRecords = true
                        isNonnullAnnotation = true
                        withNonnullAnnotationType("org.jetbrains.annotations.NotNull")
                        isImmutablePojos = true
                        isPojosAsJavaRecordClasses = true
                        isFluentSetters = false
                        isDaos = true
                        isSpringAnnotations = true
                        isRecordsImplementingRecordN = true
                    }
                    target.apply {
                        packageName = "de.romqu.trdesktopapi"
                        directory = "src/generated/jooq"
                    }
                    strategy.name = "EntityStrategy"
                }
            }
        }
    }
}

buildscript {
    configurations["classpath"].resolutionStrategy.eachDependency {
        if (requested.group == "org.jooq") {
            if (requested.name == "joor") useVersion("0.9.13")
            else useVersion("3.14.7")
        }
    }
}


