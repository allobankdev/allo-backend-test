plugins {
	java
	id("org.springframework.boot") version "3.5.10"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "co.id.allobank"
version = "0.0.1-SNAPSHOT"
description = "Finance Service for Backend Test Allo Bank"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    compileOnly("org.projectlombok:lombok")
    compileOnly("io.soabase.record-builder:record-builder-core:44")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("io.soabase.record-builder:record-builder-processor:44")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.assertj:assertj-core")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
