<h1 align="center">Trébol Backend Monolith</h1>

<div align="center">

  <a href="https://spring.io">
    <img src="https://github.com/spring-projects/spring-framework/raw/main/framework-docs/src/docs/spring-framework.png"
    height="120" alt="Spring Logo">
  </a>

  <!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-13-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

  [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=trebol-ecommerce_spring-boot-backend&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=trebol-ecommerce_spring-boot-backend)
  [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=trebol-ecommerce_spring-boot-backend&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=trebol-ecommerce_spring-boot-backend)
  [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=trebol-ecommerce_spring-boot-backend&metric=coverage)](https://sonarcloud.io/summary/new_code?id=trebol-ecommerce_spring-boot-backend)

  A monolithic, [Spring Boot v2.6](https://docs.spring.io/spring-boot/docs/2.6.12/reference/html/)-based backend application for the eCommerce project Trébol.

</div>

## Current Status 📓

This project's artifact, group and version have changed recently.
Now down to `v0.1.0`, as the required conditions for me to call it reaching a valid `v1.0` state have changed.

Please take a look at the `CHANGELOG.md` file to review this and other recent changes and additions.

Currently bound to and implementing [Trébol API v1.5.0](https://github.com/trebol-ecommerce/api/blob/main/trebol-api.json).

## Features 🚀

* Uses [Project Lombok](https://projectlombok.org)
  * You should have the correct plugin in your IDE to integrate with it.
  * Here's a list of [compatible software with their installation guides](https://projectlombok.org/setup/)
* Exposes a [RESTful API](https://github.com/trebol-ecommerce/trebol-api)
  * Supports all operations as described by the document
  * Filtering, sorting and pagination of data are implemented through query params
  * Login, registration and guest accounts
  * Checking out with user or guest accounts
* Uses [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
  * Annotated entity classes; including constraints and indexes where they are most needed at scale
    * Bundles drivers for H2 and MariaDB, but can virtually connect to any JDBC-compatible database with the correct driver
* Uses [Spring Security](https://spring.io/projects/spring-security)
  * Implements stateless session authentication/autorization through [JSON Web Tokens](https://jwt.io/) with aid from the [JJWT library](https://github.com/jwtk/jjwt)
    * Paired with `users`, `roles`, and `permissions` database tables (see `/src/main/resources/data.sql` for an example setup with 4 roles and users)
    * Do note that Authorities required in some controllers are hard-coded. These must match entries in the `permissions` table
  * Passwords are encoded using BCrypt
* Integrates payments with [Webpay Plus](https://transbankdevelopers.cl/producto/webpay) by Transbank ([Java SDK repo](https://github.com/TransbankDevelopers/transbank-sdk-java))
  * On due time, it may be possible to integrate more popular payment services such as Paypal and Stripe
* Integrates mail notifications with [Mailgun](https://mailgun.com) (an account and API key are required)
* Self-evident properties files for configuring mission-critical parameters such as
  * CORS mappings
  * JWT secret key and duration
  * BCrypt algorithm strength
  * Webpay integration endpoints
  * Mailgun & general mail properties

### Data model diagram

![Schema](./schema.png)

This schema was designed in a couple minutes using [Azimutt](https://github.com/azimuttapp/azimutt), a MIT-licensed, handy navigation and diagram visualization tool for Entity-Relationship models.

## Getting started 👍

### Requirements

* JDK 11+
* Apache Maven 3.6.0

### Installation

After cloning the repository, run `mvn verify`, grab a drink and wait a little.
That command will:

- Download & install dependencies
- Compile the project
- Generate the WAR package file
- Install it to your local maven repo
- Run unit tests
- Check code coverage

**The important step here is to compile the project first**, because some classes (QueryDSL types such as `QUser`, `QProduct` and so on) are unversioned.

Instead, these classes are generated through a Maven plugin included within the project dependencies.

If for any reason you fail to compile, please run `mvn clean generate-sources` and try it again.

Also don't forget to install any [Project Lombok plugin for your IDE](https://projectlombok.org/setup/) if you don't have it already.

### How to use

You can quickly run the application over an embedded server by executing `mvn spring-boot:run`

Mailgun integration will only be available if the `mailgun` profile is active. Please read the `application-mailgun.empty.properties` file and [this bit of the Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles) to know how to proceed with that.

### Configuration

The default configuration profile, which is located at `/src/main/resources/application.properties`
contains sane default settings and brief summaries of what everything does.

You _can_ run the application out-of-the-box with these, though you should have a look at it.

Also remember, that Spring Boot does support using more than one profile at once.
[This](https://docs.spring.io/spring-boot/docs/2.6.12/reference/html/features.html#features.external-config) and
[this](https://docs.spring.io/spring-boot/docs/2.6.12/reference/html/features.html#features.profiles) section of the Spring Boot guide can help you understand these mechanisms.

## Contributing to this repository 😍

Please review the [contributing guidelines](https://github.com/trebol-ecommerce/spring-boot-backend/blob/main/CONTRIBUTING.md) before proceeding.

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center"><a href="http://amigoscode.com"><img src="https://avatars.githubusercontent.com/u/40702606?v=4?s=100" width="100px;" alt="Amigoscode"/><br /><sub><b>Amigoscode</b></sub></a><br /><a href="#ideas-amigoscode" title="Ideas, Planning, & Feedback">🤔</a></td>
      <td align="center"><a href="http://benjaminlamadrid.cl"><img src="https://avatars.githubusercontent.com/u/68207359?v=4?s=100" width="100px;" alt="bglamadrid"/><br /><sub><b>bglamadrid</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=bglamadrid" title="Code">💻</a> <a href="#design-bglamadrid" title="Design">🎨</a></td>
      <td align="center"><a href="https://github.com/trangntt-016"><img src="https://avatars.githubusercontent.com/u/60552188?v=4?s=100" width="100px;" alt="Trang Nguyen"/><br /><sub><b>Trang Nguyen</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=trangntt-016" title="Code">💻</a></td>
      <td align="center"><a href="https://github.com/ParundeepSingh"><img src="https://avatars.githubusercontent.com/u/52928589?v=4?s=100" width="100px;" alt="Parundeep Singh"/><br /><sub><b>Parundeep Singh</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=ParundeepSingh" title="Code">💻</a></td>
      <td align="center"><a href="https://markus.mutas.dev"><img src="https://avatars.githubusercontent.com/u/25075900?v=4?s=100" width="100px;" alt="Markus Mutas"/><br /><sub><b>Markus Mutas</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=mutasDev" title="Code">💻</a></td>
      <td align="center"><a href="https://github.com/vaishakhvh"><img src="https://avatars.githubusercontent.com/u/72062381?v=4?s=100" width="100px;" alt="vaishakhvh"/><br /><sub><b>vaishakhvh</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=vaishakhvh" title="Code">💻</a></td>
      <td align="center"><a href="https://github.com/NyorJa"><img src="https://avatars.githubusercontent.com/u/8148370?v=4?s=100" width="100px;" alt="Rod Fetalvero"/><br /><sub><b>Rod Fetalvero</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=NyorJa" title="Code">💻</a> <a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=NyorJa" title="Tests">⚠️</a> <a href="#ideas-NyorJa" title="Ideas, Planning, & Feedback">🤔</a> <a href="#maintenance-NyorJa" title="Maintenance">🚧</a> <a href="https://github.com/trebol-ecommerce/spring-boot-backend/pulls?q=is%3Apr+reviewed-by%3ANyorJa" title="Reviewed Pull Requests">👀</a></td>
    </tr>
    <tr>
      <td align="center"><a href="https://mepox.github.io/"><img src="https://avatars.githubusercontent.com/u/21198248?v=4?s=100" width="100px;" alt="mepox"/><br /><sub><b>mepox</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=mepox" title="Code">💻</a> <a href="#maintenance-mepox" title="Maintenance">🚧</a> <a href="#ideas-mepox" title="Ideas, Planning, & Feedback">🤔</a> <a href="https://github.com/trebol-ecommerce/spring-boot-backend/pulls?q=is%3Apr+reviewed-by%3Amepox" title="Reviewed Pull Requests">👀</a></td>
      <td align="center"><a href="https://github.com/shivam-Purohit"><img src="https://avatars.githubusercontent.com/u/91889807?v=4?s=100" width="100px;" alt="Shivam Purohit"/><br /><sub><b>Shivam Purohit</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=shivam-Purohit" title="Code">💻</a></td>
      <td align="center"><a href="https://github.com/ujwalkumar1995"><img src="https://avatars.githubusercontent.com/u/20976813?v=4?s=100" width="100px;" alt="Ujwal Kumar"/><br /><sub><b>Ujwal Kumar</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=ujwalkumar1995" title="Code">💻</a></td>
      <td align="center"><a href="https://github.com/Angel-M-R"><img src="https://avatars.githubusercontent.com/u/16781447?v=4?s=100" width="100px;" alt="angelmr"/><br /><sub><b>angelmr</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=Angel-M-R" title="Code">💻</a></td>
      <td align="center"><a href="https://github.com/Prashriya"><img src="https://avatars.githubusercontent.com/u/66111954?v=4?s=100" width="100px;" alt="Prashriya Acharya"/><br /><sub><b>Prashriya Acharya</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=Prashriya" title="Code">💻</a></td>
      <td align="center"><a href="https://github.com/logesr"><img src="https://avatars.githubusercontent.com/u/55475935?v=4?s=100" width="100px;" alt="Loges R"/><br /><sub><b>Loges R</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=logesr" title="Code">💻</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
