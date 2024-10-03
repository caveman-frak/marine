plugins {
    `java-library`
    id("marine.java-conventions")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    api(libs.findLibrary("spring-boot-actuator").get())
}