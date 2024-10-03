plugins {
    id("marine.application-conventions")
    id("marine.tailwind-build")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findBundle("html").get())
    runtimeOnly(libs.findBundle("html-rt").get())
}

testing {
    suites {
        val integrationTest by getting(JvmTestSuite::class) {
            dependencies {
                implementation.add(libs.findLibrary("html-unit").get())
            }
        }
    }
}