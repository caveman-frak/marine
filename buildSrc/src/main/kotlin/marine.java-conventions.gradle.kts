@file:Suppress("UnstableApiUsage")

plugins {
    id("java")
    id("jvm-test-suite")
    id("java-test-fixtures")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("io.freefair.lombok")
    id("idea")
    id("com.adarshr.test-logger")
}

group = "co.uk.bluegecko.marine"
version = "1.0"
description = "Marine Tracking System"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.ORACLE)
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("org.hibernate.orm:hibernate-core")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-protobuf")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    implementation("org.apache.commons:commons-text:1.12.0")
    implementation("org.mapstruct:mapstruct:1.6.0.Beta2")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0.Beta2")
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation("org.instancio:instancio-junit:4.6.0")
    testFixturesImplementation("net.datafaker:datafaker:2.2.2")
    implementation("com.github.spotbugs:spotbugs-annotations:4.8.6")
}

testing {
    suites {
        val applySpringTest = { suite: JvmTestSuite ->
            suite.dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
                implementation("org.junit-pioneer:junit-pioneer:2.2.0")
                implementation("org.awaitility:awaitility")
                implementation("uk.org.webcompere:model-assert:1.0.0")
                implementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")
                implementation("org.xmlunit:xmlunit-assertj3:2.10.0")
                implementation("com.google.jimfs:jimfs:1.0")
                implementation("com.github.stefanbirkner:system-lambda:1.2.1")
                implementation("net.jqwik:jqwik:1.9.0")
                implementation("com.github.valfirst:slf4j-test:3.0.1")
                configurations.all {
                    exclude("ch.qos.logback", "logback-classic")
                }
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
    if (!project.hasProperty("createReports")) {
        reports.html.required = false
        reports.junitXml.required = false
    }
}
tasks.withType<Test> {
    jvmArgs(setOf("-XX:+EnableDynamicAgentLoading"))
}
tasks.named<ProcessResources>("processResources") {
    filesMatching("*application*.yaml") {
        expand(project.properties)
    }
}