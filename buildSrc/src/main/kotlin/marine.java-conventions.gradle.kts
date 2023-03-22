plugins {
    id("java")
    id("jvm-test-suite")
    id("org.springframework.boot")
}

group = "co.uk.bluegecko.marine"
version = "1.0"

java {
    setSourceCompatibility(JavaVersion.VERSION_19)
    setTargetCompatibility(JavaVersion.VERSION_19)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testCompileOnly("org.projectlombok:lombok:1.18.26")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.26")
}