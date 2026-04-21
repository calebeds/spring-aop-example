plugins {
	java
	id("org.springframework.boot") version "3.5.13"
	id("io.spring.dependency-management") version "1.1.7"
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
	runtimeOnly("com.h2database:h2")

	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	testCompileOnly("org.projectlombok:lombok:1.18.36")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	implementation("org.springframework.boot:spring-boot-starter-validation")
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