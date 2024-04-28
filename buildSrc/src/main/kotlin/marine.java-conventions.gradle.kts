plugins {
    id("java")
    id("jvm-test-suite")
    id("java-test-fixtures")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("io.freefair.lombok")
    id("idea")
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
    implementation("org.mapstruct:mapstruct:1.6.0.Beta1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

testing {
    suites {
        val applySpringTest = { suite: JvmTestSuite ->
            suite.dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }

        withType<JvmTestSuite> {
            useJUnitJupiter()
            applySpringTest(this)
        }

        val test by getting(JvmTestSuite::class)
    }
}