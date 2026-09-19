plugins {
    id("java-library")
    id("idea")
    id("net.neoforged.moddev") version "2.0.147"
}

version = "${property("minecraft_version")}-${property("mod_version")}"
group = "${property("mod_group_id")}"

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
}

tasks.withType<Javadoc>().configureEach {
    isFailOnError = false
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

sourceSets.main {
    resources {
        srcDir("src/generated/resources")

        exclude("**/*.bbmodel")

        exclude("src/generated/**/.cache")
    }
}

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://maven.createmod.net")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    maven("https://maven.ithundxr.dev/snapshots")
    maven("https://maven.blamejared.com")
    maven("https://krystalsmaven.oreostack.uk/releases")
    maven("https://krystalsmaven.oreostack.uk/snapshots")
    maven("https://repo.sleeping.town/") {
        content {
            includeGroup("dev.emi")
        }
    }
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.ryanhcode.dev/releases")

    maven("https://api.modrinth.com/maven") {
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.realrobotix.me/master/") {
        content {
            includeGroup("com.rbasamoyai")
        }
    }
    maven("https://maven.latvian.dev/releases") {
        content {
            includeGroup("dev.latvian.mods")
            includeGroup("dev.latvian.apps")
        }
    }

    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.rtyley")
        }
    }
}

base {
    archivesName.set(property("mod_id").toString())
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

neoForge {
    version = property("neo_version").toString()

    parchment {
        mappingsVersion.set(property("parchment_mappings_version").toString())
        minecraftVersion.set(property("parchment_minecraft_version").toString())
    }

    runs {
        create("client") {
            client()

            systemProperty("neoforge.enabledGameTestNamespaces", property("mod_id").toString())
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", property("mod_id").toString())
        }
        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", property("mod_id").toString())
        }

        create("data") {
            data()

            programArguments.addAll(
                "--mod", property("mod_id").toString(),
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")

            logLevel.set(org.slf4j.event.Level.DEBUG)
        }
    }

    mods {
        create(property("mod_id").toString()) {
            sourceSet(sourceSets.main.get())
        }
    }
}

val localRuntime: Configuration = configurations.create("localRuntime")
configurations.runtimeClasspath {
    extendsFrom(localRuntime)
}

dependencies {
    implementation("com.simibubi.create:create-${property("minecraft_version")}:${property("create_version")}:slim") { isTransitive = false }
    implementation("net.createmod.ponder:ponder-neoforge:${property("ponder_version")}+mc${property("minecraft_version")}")
    compileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${property("minecraft_version")}:${property("flywheel_version")}")
    runtimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${property("minecraft_version")}:${property("flywheel_version")}")
    implementation("com.tterrag.registrate:Registrate:${property("registrate_version")}")

//    compileOnly("mezz.jei:jei-${property("minecraft_version")}-common-api:${property("jei_version")}")
//    compileOnly("mezz.jei:jei-${property("minecraft_version")}-neoforge-api:${property("jei_version")}")
//    runtimeOnly("mezz.jei:jei-${property("minecraft_version")}-neoforge:${property("jei_version")}")

//    compileOnly("dev.emi:emi-neoforge:${property("emi_version")}:api")

    //implementation("com.drmangotea:tfmg:${property("minecraft_version")}-${property("tfmg_version")}") { isTransitive = false }

    implementation("com.drmangotea:tfmg:${property("minecraft_version")}-${property("tfmg_version")}-build.207") { isTransitive = false }

    implementation("dev.latvian.mods:kubejs-neoforge:${property("kubejs_version")}")
    implementation("dev.latvian.mods:rhino-neoforge:${property("rhino_version")}")

    runtimeOnly("me.djtheredstoner:DevAuth-neoforge:1.2.1")
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to project.property("minecraft_version") as String,
        "minecraft_version_range" to project.property("minecraft_version_range") as String,
        "neo_version" to project.property("neo_version") as String,
        "neo_version_range" to project.property("neo_version_range") as String,
        "loader_version_range" to project.property("loader_version_range") as String,
        "mod_id" to project.property("mod_id") as String,
        "mod_name" to project.property("mod_name") as String,
        "mod_license" to project.property("mod_license") as String,
        "mod_version" to project.property("mod_version") as String,
        "mod_authors" to project.property("mod_authors") as String,
        "mod_credits" to project.property("mod_credits") as String,
        "mod_description" to project.property("mod_description") as String,
        "tfmg_version" to project.property("tfmg_version") as String,
        "kubejs_version" to project.property("kubejs_version") as String
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)

    filesMatching("**/*.java") {
        exclude()
    }

    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}
sourceSets.main.get().resources.srcDir(generateModMetadata)

neoForge {
    ideSyncTask(generateModMetadata)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true

        for (fileName in listOf("run", "out", "logs")) {
            excludeDirs.add(file(fileName))
        }
    }
}