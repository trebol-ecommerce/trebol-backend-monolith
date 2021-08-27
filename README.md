# Trébol Backend

This application is the main backend to the virtual web store app Trébol. 
It's meant to implement the 
[Trébol API](https://github.com/trebol-ecommerce/trebol-api).
Written using Java 8, Maven and current libraries and frameworks:
* Spring Boot
* Spring Web MVC
* Spring Data JPA w/ QueryDSL
* Spring Security
* JJWT
* Transbank/Webpay Plus SDK

## Requirements

* JDK 8+
* Apache Maven 3.6.0

## Configuration & Execution

Base settings are set in the default configuration profile, which is located 
at `/src/main/resources/application.properties`.
You can "just" run the application out-of-the-box with these, though you'll 
probably want to change the database driver and URL, between other things.
The aforementioned file also has instructions to set things up and running, so 
go ahead and read it if you're curious.
