# Trébol Backend
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-3-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

This application powers a complete implemented backend for the eCommerce project Trébol.

## Features:
* A [REST API designed accordingly to the OpenAPI 3 standard](https://github.com/trebol-ecommerce/trebol-api).
* Compatibility with any JDBC driver supported by Spring Data JDBC. Default bundle comes with H2 and MariaDB dependencies.
* Supports managing information about billing/shipping addresses, customers, products, images, categories, sales, salespeople, shippers, users, user roles.
* Stateless session authentication/autorization, pairing JWT with a entity-relationship model of users, roles, and permissions.
* Passwords are encoded using BCrypt.
* Supports registering new user accounts and requesting checkout-only temporary guest sessions.
* Currently only allows payment using Webpay Plus (service that accepts chilean credit and debit cards).
* And all other Spring Boot/Data JPA/Security goodness!

## Roadmap

Please see the issues section.

## Requirements

* JDK 11+
* Apache Maven 3.6.0

## Configuration & Execution

Base settings are set in the default configuration profile, which is located
at `/src/main/resources/application.properties`.
You can "just" run the application out-of-the-box with these, though you'll
probably want to change the database driver and URL, between other things.
The aforementioned file also has instructions to set things up and running, so
go ahead and read it if you're curious.

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
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
