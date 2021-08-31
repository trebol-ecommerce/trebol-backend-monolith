# Trébol Backend
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

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

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="http://amigoscode.com"><img src="https://avatars.githubusercontent.com/u/40702606?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Amigoscode</b></sub></a><br /><a href="#ideas-amigoscode" title="Ideas, Planning, & Feedback">🤔</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!