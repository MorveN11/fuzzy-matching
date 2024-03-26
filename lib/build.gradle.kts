plugins {
  `java-library`
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
  api("org.apache.commons:commons-math3:3.6.1")
  implementation("com.google.guava:guava:31.1-jre")
  implementation("com.intuit.fuzzymatcher:fuzzy-matcher:1.2.1")
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}
