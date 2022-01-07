# Trébol Backend
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-6-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=trebol-ecommerce_spring-boot-backend&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=trebol-ecommerce_spring-boot-backend)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=trebol-ecommerce_spring-boot-backend&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=trebol-ecommerce_spring-boot-backend)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=trebol-ecommerce_spring-boot-backend&metric=coverage)](https://sonarcloud.io/summary/new_code?id=trebol-ecommerce_spring-boot-backend)

This application powers a complete implemented backend for the eCommerce project Trébol using Spring Boot v2.3.


## Features:

* Exposes a [REST API designed accordingly to the OpenAPI 3 standard](https://github.com/trebol-ecommerce/trebol-api)
  * Supports all operations as described by the document
  * Filtering, sorting and pagination of data is implemented through query params
  * Login, registration and guest accounts (for doing one-time checkout)
* Uses Spring Data JPA
  * Annotated entity classes; including constraints and indexes where they are most needed at scale
    * Bundles drivers for H2 and MariaDB, but can virtually connect to any JDBC-compatible database with the correct driver
    * [Simple DB schema diagram](https://github.com/trebol-ecommerce/spring-boot-backend/blob/main/schema.png)
* Uses Spring Security
  * Stateless session authentication/autorization with JWT
    * Paired with `users`, `roles`, and `permissions` database tables (see `/src/main/resources/data.sql` for an example setup with 4 roles and users)
    * Do note that Authorities required in some controllers are hard-coded. These must match entries in the `permissions` table
  * Passwords are encoded using BCrypt
* Currently only allows checking out with [Webpay Plus by Transbank](https://transbankdevelopers.cl/producto/webpay) (chilean service, accepts credit and debit cards)
  * On due time, it may be possible to integrate more popular payment services such as Paypal and Stripe
* Human-friendly sample properties files for configuring mission-critical parameters such as:
  * CORS mappings
  * JWT secret key and duration
  * BCrypt algorithm strength
  * Webpay integration endpoints
* And all other Spring Boot goodness!


## Status

Supporting [API v1.2.1](https://github.com/trebol-ecommerce/api/releases/tag/v1.2.1).


## Requirements

* JDK 11+
* Apache Maven 3.6.0


## Getting started

After cloning the repository, I recommend to run `mvn verify`, grab a drink and wait a little. That command will download/install dependencies, compile the project, generate the WAR package file, install it to your local maven repo, run unit tests and check code coverage.

__The important step here is to compile the project first__, because some classes are not versioned and instead must be generated through a Maven plugin that is provided in the project dependencies

Once you're done with the above, you can quickly run the application with `mvn spring-boot:run`


### Configuration

The default configuration profile, which is located at `/src/main/resources/application.properties` contains sane default settings. You can "just" run the application out-of-the-box with these, though you'll probably want to change the database driver and URL, between other things.

The aforementioned file also has written instructions to set things up and running, please read it


## Contributing to this repository

Please review the [contributing guidelines](https://github.com/trebol-ecommerce/spring-boot-backend/blob/main/CONTRIBUTING.md) before proceeding.


## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="http://amigoscode.com"><img src="https://avatars.githubusercontent.com/u/40702606?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Amigoscode</b></sub></a><br /><a href="#ideas-amigoscode" title="Ideas, Planning, & Feedback">🤔</a></td>
    <td align="center"><a href="http://benjaminlamadrid.cl"><img src="https://avatars.githubusercontent.com/u/68207359?v=4?s=100" width="100px;" alt=""/><br /><sub><b>bglamadrid</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=bglamadrid" title="Code">💻</a> <a href="#design-bglamadrid" title="Design">🎨</a></td>
    <td align="center"><a href="https://github.com/trangntt-016"><img src="https://avatars.githubusercontent.com/u/60552188?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Trang Nguyen</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=trangntt-016" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/ParundeepSingh"><img src="https://avatars.githubusercontent.com/u/52928589?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Parundeep Singh</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=ParundeepSingh" title="Code">💻</a></td>
    <td align="center"><a href="https://markus.mutas.dev"><img src="https://avatars.githubusercontent.com/u/25075900?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Markus Mutas</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=mutasDev" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/vaishakhvh"><img src="https://avatars.githubusercontent.com/u/72062381?v=4?s=100" width="100px;" alt=""/><br /><sub><b>vaishakhvh</b></sub></a><br /><a href="https://github.com/trebol-ecommerce/spring-boot-backend/commits?author=vaishakhvh" title="Code">💻</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
