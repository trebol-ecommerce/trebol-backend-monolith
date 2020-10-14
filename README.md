# Trebol REST WebService

This application is the backend to the virtual web store app Trebol. It's written with Java 8, using Maven and some cool libraries and frameworks:
* Spring Boot
* Spring Web MVC
* Spring Data JPA w/ QueryDSL
* Spring Security
* JJWT

## Requirements

* JDK 7+
* Apache Maven 3.6.0
* A running database instance (the schema was built using MariaDB, its version is declared in the `pom.xml` file)

## Configuration

Most common environment settings such as the server port and the connection details for the database are present in the default configuration profile in `/src/main/resources/application.properties`. You may edit that file if you want to try the application ASAP. 
However, I recommend to copy-paste it to a new file and append a suffix to its name, e.g. `application-dev.properties`, so to keep profiles depending on the environment you're gonna use it on.

Some core features remain hard-coded in the `cl.blm.trebol.store.config` package. This is expected to change in future releases.

## Use

### Testing with Spring Boot embedded Tomcat

#### If you edited the `/src/main/resources/application.properties` file

Simply do `mvn spring-boot:run` to run the app with those settings.

#### If you created additional profiles

Run the same command with the option `-Dspring-boot.run.arguments="--spring.profiles.active=%profile%"`, where `%profile%` is the filename suffix.
For example, if your profile is called `application-dev.properties`, you'd have to execute it like `mvn -Dspring-boot.run.arguments="--spring.profiles.active=dev" spring-boot:run`

### Compile and Package

Run `mvn clean install` on the root directory. By default, the `.war` file is dropped inside `/target`. Most application servers can serve the application from it.
You can execute the `.war` directly with `java -jar trebol-backend-rest-api-%version%.war`. Make sure to replace `%version%` with the version you're using.
And, if you created profiles as explained above, you can attach them appending the parameter `--spring.profiles.active=%profile%`.