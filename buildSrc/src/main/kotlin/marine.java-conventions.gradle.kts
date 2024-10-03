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
    implementation(platform(libs.findLibrary("telemetry-bom").get()))
    implementation(libs.findBundle("spring-boot-starter").get())
    implementation(libs.findLibrary("hibernate").get())
    implementation(libs.findLibrary("spring.doc").get())
    implementation(libs.findBundle("jackson").get())
    implementation(libs.findLibrary("logback-encoder").get())
    implementation(libs.findLibrary("commons-text").get())
    implementation(libs.findBundle("mapstruct").get())
    annotationProcessor(libs.findLibrary("mapstruct-apt").get())
    testFixturesImplementation(libs.findBundle("spring-boot-test").get())
    testFixturesImplementation(libs.findBundle("fake-data").get())
    implementation(libs.findLibrary("spotbugs").get())
    testImplementation(libs.findLibrary("slf4j-test").get())
}

testing {
    suites {
        val applySpringTest = { suite: JvmTestSuite ->
            suite.dependencies {
                implementation(platform(SpringBootPlugin.BOM_COORDINATES))
                implementation(project())
                implementation(testFixtures(project()))
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