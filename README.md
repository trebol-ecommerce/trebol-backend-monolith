<h1 align="center">Trébol Backend Monolith</h1>

<div align="center">

  <a href="https://spring.io/projects/spring-boot">
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
built using [Spring Boot v2.6](https://docs.spring.io/spring-boot/docs/2.6.x/reference/html/).

Since then it's been migrated migrated to [Spring Boot v3.2](https://docs.spring.io/spring-boot/docs/3.2.x/reference/html/).

</div>

## Current Status

Recently, this project has been set up to work with Java 17,
and as such, the artifact id of the project object model was changed to reflect on that.

Currently, this backend implementation is aligned with [the Trébol API v3.0](https://github.com/trebol-ecommerce/api/blob/v3.0.0/src/trebol-api.json).
But at the same time, this application includes endpoints that are beyond the scope of that API.
These enable some important features:

* Registering an user account
* Logging in with said account
* Placing orders using a guest account
* Querying level of authorization per-module

Please take a look at the [CHANGELOG file](CHANGELOG.md)
for full explanation on the most recent changes, additions and fixes.

## Features

* Exposes a [RESTful API](https://github.com/trebol-ecommerce/trebol-api)
  and supports all the operations described by the document, such as:
  * CRUD operations on all declared data types.
    * Can filter, sort and paginate through query params.
    * Some endpoints support partial updates using `PATCH` requests.
  * Login, registration and optionally, guest customer accounts.
  * Checking out as a registered user or a guest (when enabled).
* Uses [Project Lombok](https://projectlombok.org)
  in all of its API models and JPA entities for an easier time reading.
* Uses [Spring Data JPA](https://spring.io/projects/spring-data-jpa).
  * Includes annotated entity classes; specifying constraints and indexes where
    they'll be most likely needed when scaling up.
  * Bundles drivers for H2 and MariaDB, but can virtually connect to any JDBC-compatible database
    with the correct driver.
* Uses [Spring Security](https://spring.io/projects/spring-security).
  * Implements stateless authentication/authorization through [JSON Web Tokens](https://jwt.io/)
    by using [JJWT](https://github.com/jwtk/jjwt).
    * Constrained in tandem with database tables `users`, `roles`, and `permissions`.
      * See [data.sql](/src/main/resources/data.sql) for an example setup with 4 roles and users.
    * Do note that the Authorities required in REST controllers are hard-coded.
      These must match entries in the `permissions` database table.
  * Passwords encoded with BCrypt by default.
* Integrates payments with [WebpayPlus](https://transbankdevelopers.cl/producto/webpay)
  by Transbank ([Java SDK repo](https://github.com/TransbankDevelopers/transbank-sdk-java)).
  * It is planned to integrate more internationally popular payment services such as PayPal and Stripe.
* Integrates mail notifications with [Mailgun](https://mailgun.com)
  (an account and API key are required).
* Defines self-evident application properties, and provides a throughfully-explained example file with them,
  with sane defaults for quickly testing in your local machine.
  * Mission-critical properties, such as...
    * Payments with WebpayPlus.
    * Mailgun & general properties for mail-related services.
  * Security-crucial properties; the likes of...
    * CORS mappings, should one need to remap the API.
    * JWT secret key and duration.
    * BCrypt algorithm strength for storing passwords.

### Data model diagram

There was an Entity-Relationship diagram included in this part of the README file here,
but after some of the recent, more heavy changes, it became outdated so it was deleted.
_It's not very good practice to store binary data either..._

**However, please remember the repo does contain [the schema file](/src/main/resources/data.sql)
to spin up a new database from**.
If you need/want to have a diagram to look at, I recommend trying [Azimutt](https://github.com/azimuttapp/azimutt)
to create and design one from it. It's a powerful little program, _really intuitive and easy to use too_.

## Getting started

### Requirements

* [A supported version of the JDK](https://whichjdk.com) (see table below)
* [Apache Maven 3](https://maven.apache.org)

#### Supported JDK versions

| Java version | Supported? |
|--------------|------------|
| < 17         | no         |
| 17           | yes        |
| 19           | compatible |
| 21           | compatible |

### Installation

First and foremost, please don't forget to download, install and enable
[the correct Project Lombok plugin for your IDE](https://projectlombok.org/setup/)
if you haven't done so by now.

After cloning the repository, run `mvn verify` and get yourself comfortable because it'll take a while.
That command will:

1. Download & install dependencies.
2. Generate some of the source code.
3. Compile the project.
4. Generate the WAR package file.
5. Install it to your local maven repo.
6. Run unit tests.
7. Check code coverage.

**One very important step** that may confuse some, is no. 2, generating some source code.
We have set up type-safe queries by relying on dynamically-created QueryDSL classes such as `QUser` and `QProduct`,
which are un-versioned and only brought in to the code through a Maven plugin within the project dependencies.

So if for any reason you fail to compile the project,
please reassure said step is successful by running `mvn clean generate-sources` first.
Then try the former command again.

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
