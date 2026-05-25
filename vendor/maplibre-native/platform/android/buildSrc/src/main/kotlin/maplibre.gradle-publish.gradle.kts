import org.gradle.kotlin.dsl.get
import java.util.Locale

plugins {
    `maven-publish`
    signing
    id("com.android.library")
    id("mapvina.artifact-settings")
    id("mapvina.publish-root")
}

tasks.register<Javadoc>("androidJavadocs") {
    source = fileTree(android.sourceSets.getByName("main").java.srcDirs)
    classpath = files(android.bootClasspath)
    isFailOnError = false
}

tasks.register<Jar>("androidJavadocsJar") {
    archiveClassifier.set("javadoc")
    from(tasks.named("androidJavadocs", Javadoc::class.java).get().destinationDir)
}

tasks.register<Jar>("androidSourcesJar") {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).apply {
        charSet = "UTF-8"
        docEncoding = "UTF-8"
    }
}


artifacts {
    add("archives", tasks.named("androidSourcesJar"))
    add("archives", tasks.named("androidJavadocsJar"))
}

project.logger.lifecycle(project.extra["versionName"].toString())

version = project.extra["versionName"] as String
group = project.extra["mapVinaArtifactGroupId"] as String

fun configureMavenPublication(
    renderer: String,
    publicationName: String,
    artifactIdPostfix: String,
    descriptionPostfix: String,
    buildType: String = "Release"
) {
    publishing {
        publications {
            create<MavenPublication>(publicationName) {
                groupId = project.group.toString()
                artifactId = "${project.extra["mapVinaArtifactId"]}$artifactIdPostfix"
                version = project.version.toString()

                from(components["${renderer}${buildType}"])

                pom {
                    name.set("${project.extra["mapVinaArtifactTitle"]}$descriptionPostfix")
                    description.set("${project.extra["mapVinaArtifactTitle"]}$descriptionPostfix")
                    url.set(project.extra["mapVinaArtifactUrl"].toString())
                    licenses {
                        license {
                            name.set(project.extra["mapVinaArtifactLicenseName"].toString())
                            url.set(project.extra["mapVinaArtifactLicenseUrl"].toString())
                        }
                    }
                    developers {
                        developer {
                            id.set(project.extra["mapVinaDeveloperId"].toString())
                            name.set(project.extra["mapVinaDeveloperName"].toString())
                            email.set("team@mapvina.com")
                        }
                    }
                    scm {
                        connection.set(project.extra["mapVinaArtifactScmUrl"].toString())
                        developerConnection.set(project.extra["mapVinaArtifactScmUrl"].toString())
                        url.set(project.extra["mapVinaArtifactUrl"].toString())
                    }
                }
            }
        }
    }
}


// workaround for https://github.com/gradle/gradle/issues/26091#issuecomment-1836156762
// https://github.com/gradle-nexus/publish-plugin/issues/208
tasks {
    withType<PublishToMavenRepository> {
        dependsOn(withType<Sign>())
    }
}

afterEvaluate {
    configureMavenPublication("opengl", "defaultrelease", "", "")
    configureMavenPublication("opengl", "defaultdebug", "-debug", " (Debug)", "Debug")
    configureMavenPublication("vulkan", "vulkanrelease", "-vulkan", "(Vulkan)")
    configureMavenPublication("vulkan", "vulkandebug", "-vulkan-debug", "(Vulkan, Debug)", "Debug")
    // Right now this is the same as the first, but in the future we might release a major version
    // which defaults to Vulkan (or has support for multiple backends). We will keep using only
    // OpenGL ES with this artifact ID if that happens.
    configureMavenPublication("opengl", "openglrelease", "-opengl", " (OpenGL ES)")
    configureMavenPublication("opengl", "opengldebug", "-opengl-debug", " (OpenGL ES, Debug)", "Debug")
}


afterEvaluate {
    android.libraryVariants.forEach { variant ->
        tasks.named("androidJavadocs", Javadoc::class.java).configure {
            doFirst {
                classpath = classpath.plus(files(variant.javaCompileProvider.get().classpath))
            }
        }
    }
}

signing {
    sign(publishing.publications)
}
