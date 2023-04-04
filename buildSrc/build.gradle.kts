plugins {
    `kotlin-dsl`
    id("groovy-gradle-plugin")
}

repositories {
    mavenCentral()
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.0.5")
}