import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission

plugins {
    de.undercouch.download
}

class Tailwind {
    var version: String? = null
    var binary: String? = null
    var checksum: String? = null

    fun exec(): String {
        return "$binary-$version"
    }

    fun dir(): String {
        return rootProject.projectDir.absolutePath
    }

    fun path(): String {
        return dir() + "/.tailwindcss-binaries/" + exec()
    }

    fun url(): String {
        return "https://github.com/tailwindlabs/tailwindcss/releases/download/v$version/$binary"
    }
}

var os: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()

val tailwind = Tailwind().apply {
    version = "3.4.13"

    if (os.isMacOsX) {
        binary = "tailwindcss-macos-x64"
        checksum = "3c4423494d8204b37455cb77b1b85ef5c6c42413f58e1516a4bf7528531c067d"
    }
    if (os.isLinux) {
        binary = "tailwindcss-linux-x64"
        checksum = "c91ccc8642f79d7db5538e8d686a4dc18e00a93180f5377208a9a93c7efb9b6a"
    }
    if (os.isWindows) {
        binary = "tailwindcss-windows-x64.exe"
        checksum = "76d7a37764c172bd25f9eb2b76d46099cca642f84c8dda10891a536018ab1511"
    }
}

tasks {
    val tailwindCliDownload by registering(Download::class) {
        description = "Download Tailwind CLI"
        src(tailwind.url())
        dest(tailwind.path())
        overwrite(false)
        onlyIfNewer(true)
    }
    val tailwindCliVerify by registering(Verify::class) {
        description = "Verfify Tailwind CLI"
        dependsOn(tailwindCliDownload)
        src(File(tailwind.path()))
        algorithm("SHA-256")
        checksum(tailwind.checksum)
    }
    val tailwindCliExecutable by registering {
        description = "Ensure Tailwind CLI is executable"
        dependsOn(tailwindCliVerify)
        val perms = setOf(
            PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE
        )
        if (Files.exists(Paths.get(tailwind.path()))) {
            Files.setPosixFilePermissions(Paths.get(tailwind.path()), perms)
        }
    }
    val watchCss by registering(Exec::class) {
        description = "Watch CSS Files"
        dependsOn(tailwindCliExecutable)
        commandLine(
            tailwind.path(), "--watch=always",
            "-c", "src/main/resources/tailwind/tailwind.config.js",
            "-i", "src/main/resources/styles/input.css",
            "-o", "src/main/resources/static/styles/output.css"
        )
    }
    val buildCss by registering(Exec::class) {
        description = "Build CSS files with Tailwind"
        dependsOn(tailwindCliExecutable)
        commandLine(
            tailwind.path(),
            "-c", "src/main/resources/tailwind/tailwind.config.js",
            "-i", "src/main/resources/styles/input.css",
            "-o", "src/main/resources/static/styles/output.css"
        )
    }
}