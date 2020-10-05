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

* Most common environment options can be changed from the `.properties` files, such as the server port and the connection details for the database. It's recommended to keep the settings separated by files (as-is), but you may alter `application.properties` completely depending on your needs.
* Some core features are hard-coded in the 'cl.blm.trebol.store.config' package, including but not limiting them to the accepted `.properties` file for each separate concern.

## Compiling

* Just run `mvn clean install` on the root directory.

## Running / Deploying

* Do `mvn spring-boot:run` on the `/app` module to start an embedded Tomcat instance and serve this application on it.
* Running `mvn package` generates an EAR package that can be deployed into an application server of your choice.
