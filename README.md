[<ins>English</ins>](README.md) - [Polish](README.pl.md)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](docs/LICENSE)
[![](https://img.shields.io/github/workflow/status/bycza-zagroda/track-expenses-app-backend/run_tests.yml/develop)](https://github.com/bycza-zagroda/track-expenses-app-backend/actions)
[![codecov](https://codecov.io/gh/bycza-zagroda/track-expenses-app-backend/branch/develop/graph/badge.svg?token=WMXYJX2FWH)](https://codecov.io/gh/bycza-zagroda/track-expenses-app-backend)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bycza-zagroda_track-expenses-app-backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=bycza-zagroda_track-expenses-app-backend)
<br>
[![Follow us!](https://img.shields.io/badge/FB-Bycza%20Zagroda-blue)](https://www.facebook.com/groups/byczazagroda/about/)
[![Follow us!](https://img.shields.io/badge/DISCORD-Bycza%20Zagroda-9cf)](https://discord.gg/kPgkDGXeQw)

# track-expenses-backend

<div align="center">
  <img src="docs/images/placeholder-150.png" alt="Track Expenses app logo" />
</div>

* [About project](#about-project)
* [Initial assumptions of the application](#initial-assumptions-of-the-application)
* [Prerequisites](#prerequisites)
* [How to run](#how-to-run)
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

## How to run

### 1. Clone the repository
Please clone the repository by https or ssh (below we use the https method).
```
git clone https://github.com/bycza-zagroda/track-expenses-app-backend.git
```

### 2. Run the database
You need a working mysql server to run this application.
You can use your local server installation or use the docker compose file from this project.

**Remember:** if you are using your local server instance, change parameters for the database connection.

To run only mysql server from our docker compose configuration please enter to the directory
```
cd track-expenses-app-backend
```

and run
```
docker-compose up -d database
```
Wait until the database server starts completely, it may take a while.

### 3. Run the project

Next, you can run the project using Maven:
```
./mvnw spring-boot:run \
-Dspring-boot.run.profiles=dev \
-Dspring-boot.run.arguments="
--DB_SERVER=localhost 
--DB_PORT=3308 
--DB_NAME=trackexpensesapp 
--DB_PASSWORD=root 
--DB_USER=root"
```
Or, first, you can package as jar:
```
./mvnw package
```
Then, run:
```
java -jar target/track-expenses-app-backend-*.jar \
--spring.profiles.active=dev \
--DB_SERVER=localhost \
--DB_PORT=3308 \
--DB_NAME=trackexpensesapp \
--DB_PASSWORD=root \
--DB_USER=root 
```

Now you can navigate to http://localhost:8080 in your browser.




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
More info in [Code Of Conduct](docs/CODE_OF_CONDUCT.md) section

## Contributing
Please read the file [CONTRIBUTING.md](docs/CONTRIBUTING.md)

## Definition of Ready
Please read the file [DEFINITION_OF_READY.md](docs/definition/DEFINITION_OF_READY.md)

## Definition of Done
Please read the file [DEFINITION_OF_DONE.md](docs/definition/DEFINITION_OF_DONE.md)

## Authors
Created with ❤ by [**_bycza-zagroda_**](https://github.com/orgs/bycza-zagroda/people) community

## License
The project is covered by [_Apache License Version 2.0, January 2004_](docs/LICENSE)
