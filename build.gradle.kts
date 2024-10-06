plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.3"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("systems.manifold.manifold-gradle-plugin") version "0.0.2-alpha"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.potpvp.minecraft"
version = "1.0"
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

repositories {
    mavenCentral()
}

manifold {
    manifoldVersion.set("2024.1.34")
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.34")

    implementation("systems.manifold:manifold-json-rt:${manifold.manifoldVersion.get()}")
    implementation("systems.manifold:manifold-props-rt:${manifold.manifoldVersion.get()}")

    annotationProcessor("systems.manifold:manifold-json:${manifold.manifoldVersion.get()}")
    annotationProcessor("systems.manifold:manifold-props:${manifold.manifoldVersion.get()}")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation("systems.manifold:manifold-json-rt:${manifold.manifoldVersion.get()}")
    testImplementation("systems.manifold:manifold-props-rt:${manifold.manifoldVersion.get()}")

    testAnnotationProcessor("systems.manifold:manifold-json:${manifold.manifoldVersion.get()}")
    testAnnotationProcessor("systems.manifold:manifold-props:${manifold.manifoldVersion.get()}")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

tasks {
    compileJava {
        options.release.set(java.toolchain.languageVersion.get().asInt())
        options.encoding = "UTF-8"
    }

    runServer {
        minecraftVersion("1.21.1")
    }

    shadowJar {
        mergeServiceFiles()
    }
}

paper {
    name = "PotPvP"
    main = "io.potpvp.minecraft.PotPlugin"
    apiVersion = "1.21"
    author = "Rysefoxx"
    version = getVersion().toString()
}