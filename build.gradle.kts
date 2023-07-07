plugins {
    java
    jacoco
    application
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.springframework.boot") version "2.6.7"
    id("org.openapi.generator") version "5.4.0"
    id("io.freefair.lombok") version "6.4.0"
}

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
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

dependencies {
    implementation(enforcedPlatform("org.springframework.boot:spring-boot-dependencies:2.6.7"))
    implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2021.0.1"))

    implementation("com.datadoghq:dd-trace-api:0.100.0")
    implementation("com.datadoghq:dd-trace-ot:0.100.0")

    implementation("com.datadoghq:dd-trace:0.2.12")
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
    implementation("ch.qos.logback:logback-classic:1.2.1")
    implementation("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    implementation("ch.qos.logback.contrib:logback-jackson:0.1.5")



    implementation("de.idealo.whitelabels:logstash-logback-http:1.1.0")


    implementation("io.swagger:swagger-annotations:1.6.5")


    implementation("org.json:json:20220320")

    implementation("org.springframework.boot:spring-boot-starter-cache:2.2.2.RELEASE")
    implementation("javax.cache:cache-api:1.1.1")
    implementation("org.ehcache:ehcache:3.8.0")
    // implementation("org.apache.logging.log4j:log4j-layout-template-json:2.17.2")
    implementation("org.slf4j:jul-to-slf4j:1.7.36")
    implementation("org.slf4j:jcl-over-slf4j:1.7.36")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    implementation("org.springframework.cloud:spring-cloud-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-context")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")


    implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-fabric8")
    implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-fabric8-config")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
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

tasks.register("dependencyReportFile", DependencyReportTask::class.java) {
    group = "build"
    outputFile = file("dependencies.txt")
}

tasks.register("updateVersion", Exec::class.java) {
    group = "build setup"
    workingDir(".")
    commandLine("./update_version.sh")
}
