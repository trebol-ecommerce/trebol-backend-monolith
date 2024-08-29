<h1 align="center">Trébol Backend Monolith</h1>

<div align="center">

  <a href="https://spring.io">
    <img src="https://github.com/spring-projects/spring-framework/raw/main/framework-docs/src/docs/spring-framework.png"
    height="120" alt="Spring Logo">
  </a>

  <!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-14-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=trebol-ecommerce_spring-boot-backend&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=trebol-ecommerce_spring-boot-backend)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=trebol-ecommerce_spring-boot-backend&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=trebol-ecommerce_spring-boot-backend)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=trebol-ecommerce_spring-boot-backend&metric=coverage)](https://sonarcloud.io/summary/new_code?id=trebol-ecommerce_spring-boot-backend)

A monolithic backend web application for the eCommerce project Trébol,
built using [Spring Boot v2.6](https://docs.spring.io/spring-boot/docs/2.6.x/reference/html/)
and since then migrated to [Spring Boot v3.2](https://docs.spring.io/spring-boot/docs/3.2.x/reference/html/).

</div>

## Current Status

Although some time has passed since I started working on Trébol, it is still far from a complete project.
I have learned many things in the process: from design patterns to working with the intricacies of
Spring Boot Web application. I've also gotten better at Git itself.

Recently, this project has been set up to work with Java 21, and as such, the artifact id of the project was changed too.

Currently, this backend implementation is aligned to
[Trébol API v1.7.2](https://github.com/trebol-ecommerce/api/blob/v1.7.2/src/trebol-api.json).
It is planned to migrate towards
[version 3.0](https://github.com/trebol-ecommerce/api/blob/v3.0.0/src/trebol-api.json).

Please take a look at the [CHANGELOG file](CHANGELOG.md) to review the most recent changes, additions and fixes.

## Features

* Exposes a [RESTful API](https://github.com/trebol-ecommerce/trebol-api) and supports
  all the operations described by the document, such as
  * CRUD operations on all declared data types
    * Can filter, sort and paginate through query params
    * Some endpoints support partial updates using `PATCH` requests
  * Login, registration and optionally, guest customer accounts
  * Checking out as a registered user or a guest (when enabled)
* Uses [Project Lombok](https://projectlombok.org) in all of its API models and JPA entities
  * Here's a list of [compatible IDEs and their installation guides](https://projectlombok.org/setup/)
    to get yourself an instance running in no time
* Uses [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
  * Annotated entity classes; including constraints and indexes where they are most needed at scale
  * Bundles drivers for H2 and MariaDB; can virtually connect to any JDBC-compatible database with the correct driver
* Uses [Spring Security](https://spring.io/projects/spring-security)
  * Implements stateless session authentication/autorization through [JSON Web Tokens](https://jwt.io/)
    with aid from the [JJWT](https://github.com/jwtk/jjwt) library
    * Paired with `users`, `roles`, and `permissions` database tables
      * See [data.sql](/src/main/resources/data.sql) for an example setup with 4 roles and users)
    * Do note that Authorities required in some controllers are hard-coded
      These must match entries in the `permissions` database table
  * Passwords encoded with BCrypt by default
* Integrates payments with [WebpayPlus](https://transbankdevelopers.cl/producto/webpay)
  by Transbank ([Java SDK repo](https://github.com/TransbankDevelopers/transbank-sdk-java))
  * It is planned to integrate more internationally popular payment services such as PayPal and Stripe
* Integrates mail notifications with [Mailgun](https://mailgun.com) (an account and API key are required)
* Defines quite-evident properties, and provides a throughfully-explained example file with them,
  with sane defaults for quickly testing in your local machine.
  * Mission-critical
    * WebpayPlus properties
    * Mailgun & general mail properties
  * Security-crucial
    * CORS mappings
    * JWT secret key and duration
    * BCrypt algorithm strength

### Data model diagram

![Schema](./schema.png)

This Entity-Relationship model diagram was designed quickly using
[Azimutt](https://github.com/azimuttapp/azimutt).

## Getting started 👍

### Requirements

* [Supported JDK](https://whichjdk.com)
* [Apache Maven 3](https://maven.apache.org)

#### Supported JDK versions

| Java version | Supported? |
|--------------|------------|
| < 21         | no         |
| 21           | yes        |
| > 21         | no         |

### Installation

First and foremost, don't forget to download, install and enable
[the correct Project Lombok plugin for your IDE](https://projectlombok.org/setup/)
if you haven't done so by the time you read this.

After cloning the repository, run `mvn verify`, get yourself comfortable and wait until it is done.
That command will:

- Download & install dependencies
- Generate some of the source code
- Compile the project
- Generate the WAR package file
- Install it to your local maven repo
- Run unit tests
- Check code coverage

**An important step here that may confuse some people is the generation of source code**.
We have set up type-safe queries by relying on dynamically created QueryDSL classes such as `QUser` and `QProduct`,
which are un-versioned and only included through a Maven plugin within the project dependencies.

If for any reason you fail to compile the project,
please try running `mvn clean generate-sources` instead and then try the former command again.

### How to use

You can quickly run the application on top of an embedded server
by executing the Spring Boot Run maven goal `mvn spring-boot:run`.

Integration with Mailgun will only be available if the `mailgun` Spring profile is active.
Please read the `application-mailgun.empty.properties` file and
[this bit of the Spring Boot documentation](https://docs.spring.io/spring-boot/docs/2.6.12/reference/html/features.html#features.profiles)
to know how to proceed about said Spring profile.

### Configuration

The default configuration properties are located in the
[default properties file](src/main/resources/application.properties).
These are sane, briefly documented defaults. You _can_ run the application out-of-the-box with these.
You can also totally play around with them.

Also remember, that Spring Boot does support using more than one profile at once.
The sections in the official guide that explain
[External configuration](https://docs.spring.io/spring-boot/docs/2.6.12/reference/html/features.html#features.external-config)
and
[Profiles](https://docs.spring.io/spring-boot/docs/2.6.12/reference/html/features.html#features.profiles)
can help you understand these mechanisms.

## Contributing to this repository 😍

Please review the [contributing guidelines](./CONTRIBUTING.md) before proceeding.

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="http://amigoscode.com"><img src="https://avatars.githubusercontent.com/u/40702606?v=4?s=100" width="100px;" alt="Amigoscode"/><br /><sub><b>Amigoscode</b></sub></a><br /><a href="#ideas-amigoscode" title="Ideas, Planning, & Feedback">🤔</a></td>
      <td align="center" valign="top" width="14.28%"><a href="http://benjaminlamadrid.cl"><img src="https://avatars.githubusercontent.com/u/68207359?v=4?s=100" width="100px;" alt="bglamadrid"/><br /><sub><b>bglamadrid</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=bglamadrid" title="Code">💻</a> <a href="#design-bglamadrid" title="Design">🎨</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/trangntt-016"><img src="https://avatars.githubusercontent.com/u/60552188?v=4?s=100" width="100px;" alt="Trang Nguyen"/><br /><sub><b>Trang Nguyen</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=trangntt-016" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/ParundeepSingh"><img src="https://avatars.githubusercontent.com/u/52928589?v=4?s=100" width="100px;" alt="Parundeep Singh"/><br /><sub><b>Parundeep Singh</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=ParundeepSingh" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://markus.mutas.dev"><img src="https://avatars.githubusercontent.com/u/25075900?v=4?s=100" width="100px;" alt="Markus Mutas"/><br /><sub><b>Markus Mutas</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=mutasDev" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/vaishakhvh"><img src="https://avatars.githubusercontent.com/u/72062381?v=4?s=100" width="100px;" alt="vaishakhvh"/><br /><sub><b>vaishakhvh</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=vaishakhvh" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/NyorJa"><img src="https://avatars.githubusercontent.com/u/8148370?v=4?s=100" width="100px;" alt="Rod Fetalvero"/><br /><sub><b>Rod Fetalvero</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=NyorJa" title="Code">💻</a> <a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=NyorJa" title="Tests">⚠️</a> <a href="#ideas-NyorJa" title="Ideas, Planning, & Feedback">🤔</a> <a href="#maintenance-NyorJa" title="Maintenance">🚧</a> <a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/pulls?q=is%3Apr+reviewed-by%3ANyorJa" title="Reviewed Pull Requests">👀</a></td>
    </tr>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://mepox.github.io/"><img src="https://avatars.githubusercontent.com/u/21198248?v=4?s=100" width="100px;" alt="mepox"/><br /><sub><b>mepox</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=mepox" title="Code">💻</a> <a href="#maintenance-mepox" title="Maintenance">🚧</a> <a href="#ideas-mepox" title="Ideas, Planning, & Feedback">🤔</a> <a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/pulls?q=is%3Apr+reviewed-by%3Amepox" title="Reviewed Pull Requests">👀</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/shivam-Purohit"><img src="https://avatars.githubusercontent.com/u/91889807?v=4?s=100" width="100px;" alt="Shivam Purohit"/><br /><sub><b>Shivam Purohit</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=shivam-Purohit" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/ujwalkumar1995"><img src="https://avatars.githubusercontent.com/u/20976813?v=4?s=100" width="100px;" alt="Ujwal Kumar"/><br /><sub><b>Ujwal Kumar</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=ujwalkumar1995" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Angel-M-R"><img src="https://avatars.githubusercontent.com/u/16781447?v=4?s=100" width="100px;" alt="angelmr"/><br /><sub><b>angelmr</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=Angel-M-R" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Prashriya"><img src="https://avatars.githubusercontent.com/u/66111954?v=4?s=100" width="100px;" alt="Prashriya Acharya"/><br /><sub><b>Prashriya Acharya</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=Prashriya" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/logesr"><img src="https://avatars.githubusercontent.com/u/55475935?v=4?s=100" width="100px;" alt="Loges R"/><br /><sub><b>Loges R</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=logesr" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/mslowiak"><img src="https://avatars.githubusercontent.com/u/18486535?v=4?s=100" width="100px;" alt="Marcin Słowiak"/><br /><sub><b>Marcin Słowiak</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/trebol-backend-monolith/commits?author=mslowiak" title="Documentation">📖</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification.
Contributions of any kind welcome!
