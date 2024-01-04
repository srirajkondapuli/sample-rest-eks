logging.captureStandardOutput(LogLevel.INFO)
plugins {
    java
    jacoco
    application
    id("io.spring.dependency-management") version "1.1.4"
    id("org.springframework.boot") version "3.2.1"
    id("org.openapi.generator") version "5.4.0"
    id("io.freefair.lombok") version "8.4"
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
    }
}
// dependencyManagement {
//     val springCloudVersion = "2022.0.4"
//     imports {
//         mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
//     }
// }
apply(plugin = "io.spring.dependency-management")
apply(plugin = "org.springframework.boot")
description = "A sample Spring-Boot application which deploys to EKS."
group = "com.myown.ef.samples"
version = "0.0.0"




repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

ext {
  set("springCloudVersion", "2023.0.0")
}
dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
  }
}
// configurations {
//     all {
//         exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
//     }
// }

dependencies {
    implementation(enforcedPlatform("org.springframework.boot:spring-boot-dependencies:3.2.1"))
    // implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2023.0.0"))
    // https://mvnrepository.com/artifact/jakarta.xml.bind/jakarta.xml.bind-api
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
// https://mvnrepository.com/artifact/org.glassfish.jaxb/jaxb-runtime
implementation("org.glassfish.jaxb:jaxb-runtime:4.0.3")
// https://mvnrepository.com/artifact/javax.xml.bind/jaxb-api
implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

    // implementation("com.datadoghq:dd-trace-api:0.100.0")
    // implementation("com.datadoghq:dd-trace-ot:0.100.0")

    // implementation("com.datadoghq:dd-trace:0.2.12")
    implementation("net.logstash.logback:logstash-logback-encoder:7.1.1")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.swagger.parser.v3:swagger-parser:2.0.30")
    implementation("org.springframework.boot:spring-boot-autoconfigure")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")
    implementation("com.google.code.gson:gson:2.9.0")
    // implementation("ch.qos.logback:logback-classic:1.2.1")
    // implementation("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    // implementation("ch.qos.logback.contrib:logback-jackson:0.1.5")

// https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    // implementation("ch.qos.logback:logback-classic:1.4.8")


    // implementation("de.idealo.whitelabels:logstash-logback-http:1.1.0")


    implementation("io.swagger:swagger-annotations:1.6.5")

// https://mvnrepository.com/artifact/com.diogonunes/JCDP
    implementation("com.diogonunes:JCDP:2.0.3.1")
    implementation("org.json:json:20220320")

    implementation("org.springframework.boot:spring-boot-starter-cache:2.2.2.RELEASE")
    implementation("javax.cache:cache-api:1.1.1")
    implementation("org.ehcache:ehcache:3.8.0")
    // implementation("org.apache.logging.log4j:log4j-layout-template-json:2.17.2")
    // implementation("org.slf4j:jul-to-slf4j:1.7.36")
    // implementation("org.slf4j:jcl-over-slf4j:1.7.36")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")
// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies
    // implementation("org.springframework.cloud:spring-cloud-dependencies:2022.0.0")

    implementation("org.springframework.cloud:spring-cloud-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-context")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")


    implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-fabric8")
    implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-fabric8-config")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}



tasks.register("logInfo") {
    logging.captureStandardOutput(LogLevel.INFO)
    doFirst {
        println("A task message which is logged at INFO level")
    }
}

java.sourceSets["main"].java {
    srcDir("$buildDir/generated/src/main/java")
}



tasks.named<Jar>("jar") {
    enabled = false
}

jacoco {
    toolVersion = "0.8.7"
}

tasks.named<Test>("test") {
    doFirst {
        systemProperty("spring.profiles.active", "test")
    }
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn("test")
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

// tasks.register("dependencyReportFile", DependencyReportTask::class.java) {
//     group = "build"
//     outputFile = file("dependencies.txt")
// }

// tasks.register("updateVersion", Exec::class.java) {
//     group = "build setup"
//     workingDir(".")
//     commandLine("./update_version.sh")
// }
