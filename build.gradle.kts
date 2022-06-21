plugins {
    id("fabric-loom") version "0.11.+"
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.0"
    base
    `maven-publish`
}

base.archivesName.set("SettxiCloth")
group = "dev.isxander"
version = "1.0.2"

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://repo.woverflow.cc/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    val minecraftVersion: String by rootProject
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:1.18.2+build.+:v2")

    modImplementation("net.fabricmc:fabric-loader:0.13.+")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.0+kotlin.1.7.0")

    api("dev.isxander.settxi:settxi-core:2.3.0")
    api("dev.isxander.settxi:settxi-kotlinx-serialization:2.3.0")
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:6.+")
}

tasks {
    remapJar {
        archiveClassifier.set("fabric-1.18.2")
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("settxiCloth") {
            groupId = "dev.isxander"
            artifactId = "settxi-cloth-impl"

            from(components["java"])
        }
    }

    repositories {
        if (hasProperty("xander-repo.username") && hasProperty("xander-repo.password")) {
            maven(url = "https://maven.isxander.dev/releases") {
                credentials {
                    username = property("xander-repo.username") as? String
                    password = property("xander-repo.password") as? String
                }
            }
        }
    }
}
