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
    version = "3.4.3"

    if (os.isMacOsX) {
        binary = "tailwindcss-macos-x64"
        checksum = "bd56c3beedd1eba030099820ef9935323a76a15b2b91d317b6a1c0e6c740084a"
    }
    if (os.isLinux) {
        binary = "tailwindcss-linux-x64"
        checksum = "bd7be1b5f1ddfcf8383efe97db763d9c2d4e1547a3bb9bc19fd48439a2391ba1"
    }
    if (os.isWindows) {
        binary = "tailwindcss-windows-x64.exe"
        checksum = "32d8a1e2970977e8449600f0c4f8d07bee8ba492c83800cb786e918b0596c0d1"
    }
}

tasks {
    val tailwindCliDownload by registering(Download::class) {
        description = "Download Tailwind CLI"
        src(tailwind.url())
        dest(tailwind.path())
        overwrite(false)
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