[<ins>English</ins>](README.md) - [Polish](README.pl.md)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](doc/LICENSE)
[![](https://img.shields.io/github/workflow/status/bycza-zagroda/track-expenses-app-backend/run_tests.yml/develop)](https://github.com/bycza-zagroda/track-expenses-app-backend/actions)
<br>
[![Follow us!](https://img.shields.io/badge/FB-Bycza%20Zagroda-blue)](https://www.facebook.com/groups/byczazagroda/about/)
[![](https://img.shields.io/discord/787788929898250260?logoColor=%237289da)](https://discord.gg/5xmrnugs)

# track-expenses-backend

<p align="center">
  <img src="https://via.placeholder.com/150" alt="Track Expenses App Logo" />
</p>

* [About project](#about-project)
* [Initial assumptions of the application](#initial-assumptions-of-the-application)
* [Prerequisites](#prerequisites)
* [How to start](#how-to-start)
  - [Back-end repository](#back-end-repository)
* [Tech stack](#tech-stack)
  - [Development](#development)
  - [Testing](#testing)
* [Code of Conduct](#code-of-conduct)
* [Contributing](#contributing)
* [Authors](#authors)
* [License](#license)

## About project
This project is created by [_Bycza Zagroda_](https://github.com/bycza-zagroda) community.
The main aim for this project is for it to allow a user to track their own expenses.
The code for the Track Expenses App is separated for [back-end](https://github.com/bycza-zagroda/track-expenses-app-backend) and [front-end](https://github.com/bycza-zagroda/track-expenses-app-frontend) with
their own GitHub repository. In the application the [_REST_](https://pl.wikipedia.org/wiki/Representational_state_transfer) architecture and [_Minimum
Viable Product (MVP)_](https://www.parp.gov.pl/component/content/article/52414:minimum-viable-product) model is used.

## Initial assumptions of the application
- this is a small project in order to build and release with the best code practise.
- the base of the application is able to execute the most basic actions such as:
  _create_, _read_, _update_, _delete_, aka [CRUD](https://pl.wikipedia.org/wiki/CRUD)
- in this project we can work together, learn from each other and enlarge our skills
  or even develop them. Everything depends on which level knowledge of Spring Boot 2
  you have.

## Prerequisites
The following tools are required to start the application:
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) / [VSC](https://code.visualstudio.com/) / [Eclipse](https://www.eclipse.org/)
- [Java 17 LTS](https://openjdk.org/projects/jdk/17/)
- [MySql Workbench](https://www.mysql.com/products/workbench/) / [DBeaver](https://dbeaver.io/)
- [Git Bash](https://git-scm.com/downloads)
- [Maven 3.x](https://maven.apache.org/download.cgi)
- [Postman](https://www.postman.com/)
- [Docker](https://docs.docker.com/get-docker/) - please refer to [Setting up Docker]()

## How to start
Please clone the repository for back-end and front-end by https or ssh.

#### Back-end repository
- git clone: https://github.com/bycza-zagroda/track-expenses-app-backend.git

## Tech stack

### Development
- [Java 17 LTS](https://openjdk.org/projects/jdk/17/)
- [Spring Boot 2](https://spring.io/projects/spring-boot)
- [Spring Data](https://spring.io/projects/spring-data)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger / OpenAPI](https://swagger.io/specification/)
- [MySql (docker)](https://www.mysql.com/)
- [Liquibase](https://www.liquibase.org/)
- [Maven 3.x](https://maven.apache.org/)

### Testing
- [JUnit5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Test Containers](https://www.testcontainers.org/)

## Code of Conduct
More info in [Code Of Conduct](doc/CODE_OF_CONDUCT.md) section

## Contributing
Please read the file [CONTRIBUTING.md](doc/CONTRIBUTING.md)

## Authors
Created with ❤ by [**_bycza-zagroda_**](https://github.com/orgs/bycza-zagroda/people) community

## License
The project is covered by [_Apache License Version 2.0, January 2004_](doc/LICENSE)
