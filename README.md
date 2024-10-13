# Kitchensink - SpringBoot with JSF

This is a spring boot sample project that aims to showcase an alternative migration of
the ‘kitchensink’ JBoss application available in the Red Hat
[JBoss EAP Quickstarts GitHub repository](https://github.com/jboss-developer/jboss-eap-quickstarts/tree/HEAD/kitchensink)
from J2EE technology to spring-boot.

## Technology used

* Java 21
* gradle 8.10.2
* Spring boot 3.3.4
* JSF JoinFaces 5.3.4
* Mongo DB
* TestContainers

## Quickstart

This is the quickest setup section. An alternative to quickstart is the Custom Setup section (
see [below](#custom-setup)).

### Prerequisites

* This project was built on linux OS, for a straightforward quickstart you should have a similar OS however it is not
  mandatory.
* docker engine ( tested with 27.3.1 ) with docker compose plugin

### Getting Started

In the project root folder run

```
docker compose up -d
```

Success will give you in the log lines something similar to:

```
docker-compose logs -f springboot-app

springboot-app  | 2024-10-13T05:52:09.665Z  INFO 1 --- [kitchensink-boot] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
springboot-app  | 2024-10-13T05:52:09.678Z  INFO 1 --- [kitchensink-boot] [           main] c.e.k.app.KitchenSinkBootApplication     : Started KitchenSinkBootApplication in 3.411 seconds (process running for 3.733)

```

Point your web browser to [http://localhost:8080](http://localhost:8080)

If the port 8080 is not suitable for you, adapt the ```docker-compose.yml``` file to fit your needs.

## Custom Setup

This is an alternative to quickstart where you can make use of your own MongoDB.

### Prerequisites

* a Java21 JDK installed on you computer
* if you want to run also the integration tests that demo compatibility with the JBoss migrated application, then
  install a docker environment
  that is [compatible with TestContainers](https://java.testcontainers.org/supported_docker_environment/)

### Getting Started

In the project root folder.

**Compile**

Use gradle parameter ```-Dorg.gradle.java.home=<path to your java21 jdk>``` if your java 21 jdk is not on command line
PATH

```
./gradlew  build -x test
OR
./gradlew -Dorg.gradle.java.home=<path to your java21 jdk> build -x test
```

**Run tests**

```
./gradlew  build
```

**Run**
By default the application expects to connect to a running MongoDB located at ```localhost:27017``` and with no login
security.
If you need to customize the connectivity then modify
the [app/src/main/resources/application.properties](app/src/main/resources/application.properties) file

```
./gradlew  bootRun

```

## Project highlights

This section underlines the most interesting parts of the project.

### Structure

    .
    ├── app                        # The spring-boot standalone application module.
    ├───── src                     #
    ├──────── test                 # The test infrastructure that showcases the compatibility with JBoss EAP app
    ├── member-registration        # The application business logic
    ├───── base                    # Gradle java library submodule containing REST APIs and JSF web content
    ├───── mongo                   # Gradle java library submodule implementing the persistemnce layer abstracted by the base submodule
    └── README.md

### Compatibility Integration tests

In order to ensure the quality of this migration project, a black box testing approach was taken with the use of
TestContainers and the test suite
coded in the file ```KitchenSinkBootApplicationCompatibilityTests.java```
The test suite is designed to compare REST APIs input and output of the reference JBoss App
(deployed at testing time from the file ```app/src/test/resources/reference-app/kitchensink.war``` - the build artifact
of the JBoss git project )
against the current spring boot application.

**Limitations of the compatibility tests**

The compatibility tests do not cover also the preservation of the UI functionality ( for E2E testing a browser base
testing approach would be suitable e.g. using playwright framework ).  

