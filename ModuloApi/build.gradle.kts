import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    id("java")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api("org.javacord:javacord:3.7.0")
    api("org.yaml:snakeyaml:1.27")
    api("com.google.code.gson:gson:2.8.9")
    api("io.netty:netty-all:4.1.86.Final")

    api("org.apache.logging.log4j:log4j-slf4j-impl:2.9.0")
    api("com.google.guava:guava:31.1-jre")

    api("org.mongodb:mongodb-driver-sync:4.6.0")
    api("org.mongodb:bson:4.6.0")
    api("org.mongodb:mongodb-driver-core:4.6.0")

    api("fastutil:fastutil:5.0.9")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.named("cleanTest") { group = "verification" }


tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(19)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri(project.findProperty("repoUrl") as String? ?: System.getenv("REPO_URL") ?: "")
            credentials {
                username = project.findProperty("github_username") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("github_token") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            version = System.getenv("PROJECT_VERSION") ?: (findProperty("projectVersion").toString() ?: "")
            groupId = "fr.aytronn"
            artifactId = "modulo-api"
            pom {
                name.set("Modulo")
                description.set("You can see it as a minecraft server with plugins. Modulo will load the modules you added in the modules folder.")
                url.set("https://github.com/aytronnn/Modulo")
                licenses {
                    license {
                        name.set("GNU General Public License v3.0")
                        url.set("https://github.com/aytronnn/Modulo/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("aytronn")
                        email.set("aytronn18@gmail.com")
                    }
                }
            }
            from(components["java"])
        }
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
