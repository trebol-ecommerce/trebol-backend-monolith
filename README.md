# Spring REST Example Project

## Requirements

* JDK 7
* Apache Maven
* An Oracle 11g instance and [its drivers](https://blogs.oracle.com/dev2dev/get-oracle-jdbc-drivers-and-ucp-from-oracle-maven-repository-without-ides). 

## Compiling

1. Just run `mvn clean install` on the root directory, where the *pom.xml* file lies.

## Deploying

* `mvn spring-boot:run` on the `/app` module - Starts an embedded Tomcat instance running this application. Remember to edit the *.properties* files to fit your environment.
* `mvn package` - Generates an EAR package that can be deployed into an application server of your choice.
