# Trebol REST WebService

This application is the backend to the virtual web store app Trebol. It it written using Java 8 and some more cool stuff:
* Spring Boot
* Spring Web MVC
* Spring Data JPA w/ QueryDSL
* Spring Security
* JJWT

## Requirements

* JDK 7+
* Apache Maven 3.6.0
* A running database instance (the schema was built using MariaDB, its version is declared in the `pom.xml` file)

## Configuring / Setting it up

* Most common environment options such as the server port and the connection details for the database are present in `/src/main/resources/application.properties`. You might want to edit that file if you simply want to compile and run the application. You can also copy-paste it with an additional suffix, like `application-dev.properties`, to keep different Spring profiles for different environments.
* Some core features are hard-coded in the `cl.blm.trebol.store.config` package, do give them a look.

## Compiling

* Run `mvn clean install` on the root directory.

## Running / Deploying

### Without compiling

If you edited the `/src/main/resources/application.properties` file, you can do `mvn spring-boot:run` on the root directory to run the app with those settings. If you use different profiles, prepend the option `-Dspring-boot.run.arguments="--spring.profiles.active=%profilename%"` using the filename suffix e.g. 'dev'

### After compiling

By default, the `.war` file is dropped inside `/target`. You can run it with `java -jar trebol-backend-rest-api-%version%.war` (make sure to replace `%version%` correctly). And like above, you can attach a specific Spring profile adding the parameter `--spring.profiles.active=%profilename%` at the end.