plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.1")
	}
}


group = "me.calebeoliveira"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("com.h2database:h2")

	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	testCompileOnly("org.projectlombok:lombok:1.18.36")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.wiremock.integrations:wiremock-spring-boot:4.0.7")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("io.rest-assured:rest-assured")
}

tasks.withType<JavaCompile> {
	options.compilerArgs = listOf(
		"-parameters",
		"-Amapstruct.defaultComponentModel=spring"
	)
}

tasks.withType<Test> {
	useJUnitPlatform()
}