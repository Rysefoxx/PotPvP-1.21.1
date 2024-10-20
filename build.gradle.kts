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

val lombokVersion = "1.18.34"
val hibernateVersion = "5.5.5.Final"

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/groups/public/")
}

manifold {
    manifoldVersion.set("2024.1.34")
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.hibernate:hibernate-core:$hibernateVersion")
    compileOnly("org.hibernate:hibernate-envers:$hibernateVersion")

    library("org.springframework.boot:spring-boot-starter-data-jpa:3.2.4")
    library("org.springframework:spring-tx:6.1.5")
    library("org.springframework:spring-context-support:6.1.5")
    library("org.mariadb.jdbc:mariadb-java-client:2.7.3")
    library("org.javassist:javassist:3.29.0-GA")
    library("io.github.rysefoxx.inventory:RyseInventory-Plugin:1.6.5.26-DEV")

    implementation("systems.manifold:manifold-json-rt:${manifold.manifoldVersion.get()}")
    implementation("systems.manifold:manifold-props-rt:${manifold.manifoldVersion.get()}")

    annotationProcessor("systems.manifold:manifold-json:${manifold.manifoldVersion.get()}")
    annotationProcessor("systems.manifold:manifold-props:${manifold.manifoldVersion.get()}")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

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

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<Test> {
    jvmArgs("--enable-preview")
}

tasks.withType<JavaExec> {
    jvmArgs("--enable-preview")
}

paper {
    name = "PotPvP"
    main = "io.potpvp.minecraft.PotPlugin"
    apiVersion = "1.21"
    author = "Rysefoxx"
    version = getVersion().toString()

    loader = "io.potpvp.minecraft.PluginLibrariesLoader"
    generateLibrariesJson = true
}