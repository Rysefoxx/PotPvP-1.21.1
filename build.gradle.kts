plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.3"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "io.potpvp.minecraft"
version = "1.0"
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(23))

tasks {
    compileJava {
        options.release.set(java.toolchain.languageVersion.get().asInt())
        options.encoding = "UTF-8"
    }

    runServer {
        minecraftVersion("1.21.1")
    }
}

paper {
    name = "PotPvP"
    main = "io.potpvp.minecraft.PotPlugin"
    apiVersion = "1.20"
    author = "Rysefoxx"
    version = getVersion().toString()
}