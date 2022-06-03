plugins {
    val kotlinVersion: String by System.getProperties()

    id("fabric-loom") version "0.12.+"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    base
    `maven-publish`
}

base.archivesName.set("SettxiCloth")
group = "dev.isxander"
version = "1.0.4"

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://repo.woverflow.cc/")
    maven("https://maven.shedaniel.me/")
}

val minecraftVersion: String by rootProject
val kotlinVersion: String by System.getProperties()

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.+:v2")

    modImplementation("net.fabricmc:fabric-loader:0.14.+")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.7.4+kotlin.$kotlinVersion")

    api("dev.isxander.settxi:settxi-core:2.2.3")
    api("dev.isxander.settxi:settxi-kotlinx-serialization:2.2.3")
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:7.+")
}

tasks {
    remapJar {
        archiveClassifier.set("fabric-$minecraftVersion")
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
        register<MavenPublication>("settxi") {
            groupId = "dev.isxander"
            artifactId = "settxi-cloth-impl"

            from(components["java"])
        }
    }

    repositories {
        if (hasProperty("woverflow.username") && hasProperty("woverflow.password")) {
            println("Publishing ${project.name} to W-OVERFLOW")
            maven(url = "https://repo.woverflow.cc/releases") {
                credentials {
                    username = property("woverflow.username") as? String
                    password = property("woverflow.password") as? String
                }
            }
        }
    }
}
