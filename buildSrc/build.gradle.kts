plugins {
    `kotlin-dsl`
    `groovy-gradle-plugin`
}

repositories {
    mavenCentral()
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
    maven {
        url = uri("https://maven.xillio.com/artifactory/libs-release")
    }
}

dependencies {
    implementation(libs.spring.boot)
    implementation(libs.lombok)
    implementation(libs.download)
    implementation(libs.test.logger)
    implementation(libs.git.properties)
}