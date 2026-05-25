plugins {
    id("mapvina.dependencies")
}

tasks.register("androidNitpick") {
    doLast {
        println("Running android nitpick script")
        verifyLicenseGeneration()
    }
}

fun verifyLicenseGeneration() {
    println("Verify license generation with git diff...")
    exec {
        workingDir(rootDir)
        commandLine("python3", "scripts/validate-license.py")
    }
}
