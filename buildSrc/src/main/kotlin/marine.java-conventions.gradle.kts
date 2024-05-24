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
    implementation("org.apache.commons:commons-text:1.12.0")
    implementation("org.mapstruct:mapstruct:1.6.0.Beta1")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0.Beta1")
    testFixturesImplementation("org.instancio:instancio-junit:4.6.0")
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

tasks.withType<AbstractCopyTask> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}