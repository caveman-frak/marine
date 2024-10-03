plugins {
    id("marine.application-conventions")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("spring-shell").get())
}

testing {
    suites {
        val integrationTest by getting(JvmTestSuite::class) {
            dependencies {
            }
        }
    }
}