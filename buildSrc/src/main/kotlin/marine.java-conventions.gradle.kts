@file:Suppress("UnstableApiUsage")

import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    `jvm-test-suite`
    `java-test-fixtures`
    idea
    id("org.springframework.boot")
    id("io.freefair.lombok")
    id("com.adarshr.test-logger")
}

group = "co.uk.bluegecko.marine"
version = "1.0"
description = "Marine Tracking System"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
        vendor.set(JvmVendorSpec.ORACLE)
    }
    sourceCompatibility = JavaVersion.VERSION_23
    targetCompatibility = JavaVersion.VERSION_23
}

repositories {
    mavenCentral()
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    testFixturesImplementation(platform(SpringBootPlugin.BOM_COORDINATES))
    annotationProcessor(platform(SpringBootPlugin.BOM_COORDINATES))
//    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.8.0"))
    implementation(platform(libs.findLibrary("telemetry-bom").get()))
//    implementation("org.springframework.boot:spring-boot-starter")
//    implementation("org.springframework.boot:spring-boot-starter-json")
//    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("org.springframework.boot:spring-boot-starter-aop")
//    implementation("org.springframework.data:spring-data-jpa")
    implementation(libs.findBundle("spring-boot-starter").get())
//    implementation("org.hibernate.orm:hibernate-core")
    implementation(libs.findLibrary("hibernate").get())
//    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation(libs.findLibrary("spring.doc").get())
//    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
//    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-protobuf")
//    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor")
    implementation(libs.findBundle("jackson").get())
//    implementation(libs.findLibrary("logback-encoder").get())
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
//    implementation("org.apache.commons:commons-text:1.12.0")
    implementation(libs.findLibrary("commons-text").get())
//    implementation("org.mapstruct:mapstruct:1.6.2")
//    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation(libs.findBundle("mapstruct").get())
//    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")
    annotationProcessor(libs.findLibrary("mapstruct-apt").get())
//    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
//    testFixturesImplementation("org.springframework.boot:spring-boot-starter-json")
    testFixturesImplementation(libs.findBundle("spring-boot-test").get())
//    testFixturesImplementation("org.instancio:instancio-junit:5.0.2")
//    testFixturesImplementation("net.datafaker:datafaker:2.3.1")
//    testFixturesImplementation("com.thedeanda:lorem:2.2")
    testFixturesImplementation(libs.findBundle("fake-data").get())
//    implementation("com.github.spotbugs:spotbugs-annotations:4.8.6")
    implementation(libs.findLibrary("spotbugs").get())
//    testImplementation("com.github.valfirst:slf4j-test:3.0.1")
    testImplementation(libs.findLibrary("slf4j-test").get())
}

testing {
    suites {
        val applySpringTest = { suite: JvmTestSuite ->
            suite.dependencies {
                implementation(platform(SpringBootPlugin.BOM_COORDINATES))
                implementation(project())
                implementation(testFixtures(project()))
//                implementation("org.springframework.boot:spring-boot-starter-test")
//                implementation("org.junit-pioneer:junit-pioneer:2.2.0")
//                implementation("org.awaitility:awaitility")
//                implementation("uk.org.webcompere:model-assert:1.0.3")
//                implementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")
//                implementation("org.xmlunit:xmlunit-assertj3:2.10.0")
//                implementation("com.google.jimfs:jimfs:1.0")
//                implementation("com.github.stefanbirkner:system-lambda:1.2.1")
//                implementation("net.jqwik:jqwik:1.9.0")
                implementation.add(libs.findLibrary("spring-boot-test").get())
                implementation.bundle(libs.findBundle("testing").get())
            }
        }

        withType<JvmTestSuite> {
            useJUnitJupiter()
            applySpringTest(this)
        }

        val test by getting(JvmTestSuite::class)
    }
}

tasks.withType<AbstractCopyTask> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Test>().configureEach {
    exclude("ch.qos.logback", "logback-classic")
    if (!project.hasProperty("createReports")) {
        reports.html.required = false
        reports.junitXml.required = false
    }
    jvmArgs(setOf("--enable-preview", "-XX:+EnableDynamicAgentLoading"))
}

tasks.named<ProcessResources>("processResources") {
    filesMatching("*application*.yaml") {
        expand(project.properties)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-preview")
}