plugins {
    id("java-platform")
}

dependencies {
    constraints {
        api(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    }
}