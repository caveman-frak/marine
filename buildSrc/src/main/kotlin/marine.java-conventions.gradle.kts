plugins {
    id("java")
    id("jvm-test-suite")
    id("java-test-fixtures")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("idea")
}

group = "co.uk.bluegecko.marine"
version = "1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
        vendor.set(JvmVendorSpec.ORACLE)
    }
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
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

springBoot {
    buildInfo()
}

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok:")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testFixturesCompileOnly("org.projectlombok:lombok")
    testFixturesAnnotationProcessor("org.projectlombok:lombok")
    implementation("org.mapstruct:mapstruct:1.6.0.Beta1")
}