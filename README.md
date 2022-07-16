# Projekt Open Source: Track Expenses App Backend
Projekt tworzony przy współpracy społeczności "Bycza zagroda".
Projektem jest aplikacja do śledzenia wydatków osobistych. Aplikacja jest podzielona na
backend i frontend z osobnymi repozytoriami na GitHub. Aplikacja jest oparta na architekturze REST
i modelu Minimum Viable Product (MVP).

## Prerequisites:

Do uruchomienia aplikacji wymagana jest instalacja następujących narzędzi:
- IntelliJ IDEA / VSC/ Eclipse,
- Java 17 LTS,
- MySql Workbench/DBeaver,
- Git Bash,
- Maven 3.x,
- Postman,
- [Docker](https://docs.docker.com/get-docker/). Inslacja docker'a zalezy od systemu operacyjnego.
  Baza danych jest uruchamiana w kontenerze dockera. Nie jest to jednak obowiązkowe i do versji 1.0 można
- korzystać z bazy lokalnie
  bez docker'a

## Jak zacząć:
Należy sklonować repozytorium backend i frontend przez https lub ssh.
#### Repozytorium backend
- git clone: https://github.com/bycza-zagroda/track-expenses-app-backend.git  lub
- git clone: git@github.com:bycza-zagroda/track-expenses-app-backend.git

#### Repozytorium frontend (w terminie późniejszym)

Po sklonowaniu, aby uruchomić aplikację, należy wejść na http://localhost:8080

## Stack technologiczny - backend:
- Java 17 LTS,
- Spring Boot 2,
- Spring Data,
- Spring Security,
- Swagger,
- MySql (docker),
- Liquibase,
- Maven 3.x.

## Testy backend:
- JUnit5,
- Mockito,
- Test Containers

## Stack technologiczny - frontend:
- Typescript,
- Angular,
- Sass/SCSS,
- NgRX,
- Material,
- Yarn.

## Contributing:
Proszę przeczytać [CONTRIBUTING.md](doc/CONTRIBUTING.md) na temat "Kodeksu Postępowania dotyczący Współtwórców".

## Authors:
Work in progress.

## License:
Projekt jest objęty licencją Apache License Version 2.0, January 2004 - [LICENSE](doc/LICENSE)

### Początkowe założenia aplikacji:
- jest to mały projekt, aby można było go ukończyć z najlepszymi praktykami,
- zbudowanie rdzenia aplikacji, która wykonuje podstawowe operacje jak: dodaj, znajdź, zmodyfikuj, usuń wydatek/przychód,
- jest to projekt, w którym możemy wspólnie popracować i uczyć się od siebie nawzajem, poszerzać umiejętności lub
  też je utrwalać. Wszystko zależy od tego, na jakim etapie nauki Spring Boot 2 jesteście.

  
- --
### English version of README.md

# Project Open Source: Track Expenses App Back-end
This project is created by “Bycza Zagroda” community.
The main aim for this project is for it to allow a user to track their own expenses.
The code for the Track Expenses App is separated for back-end and front-end with
their own Github repository. In the application the REST architecture and Minimum
Viable Product (MVP) model is used.

## Prerequisites:
The following tools are required to start the application:
- IntelliJ IDEA/ VSC/ Eclipse,
- Java 17 LTS,
- MySql Workbench/ DBeaver,
- Git Bash,
- Maven 3.x,
- Postman,
- [Docker](https://docs.docker.com/get-docker/). How to set up the Docker depends on
  your operating system. For now, the database is booted on the docker. However, it is
  not required until the 1.0 version and it is allowed to use a local database.

## How to start:
Please clone the repository for back-end and front-end by https or ssh.

#### Back-end repository.
- git clone: https://github.com/bycza-zagroda/track-expenses-app-backend.git
  or
- git clone: git@github.com:bycza-zagroda/track-expenses-app-backend.git
  After that please go to the: http://localhost:8080

#### Front-end repository.
This repository will be in the near feature

## Technology Stack-back-end:
- Java 17 LTS,
- Spring Boot 2,
- Spring Data,
- Spring Security,
- Swagger,
- MySql (docker),
- Liquibase,
- Maven 3.x.

## Tests for back-end:
- JUnit5,
- Mockito,
- Test Containers

## Technology Stack-front-end:
- Typescript,
- Angular,
- Sass/SCSS,
- NgRX,
- Material,
- Yarn.

## Contributing and Code of Conduct:
Please read the file: [CONTRIBUTING.md](doc/CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](doc/CODE_OF_CONDUCT.md)

## Authors:
Work in progress.

## License:
The project is covered by Apache License Version 2.0, January 2004 – [LICENSE](doc/LICENSE)

### Initial assumptions of the application:
- this is a small project in order to build and release with the best code practise.
- the base of the application is able to execute the most valid actions such as:
  add-, find-, change, delete-element
- in this project we can work together, learn from each other and enlarge our skills
  or even develop them. Everything depends on which level knowledge of Spring Boot 2
  you have.
