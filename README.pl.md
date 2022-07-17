[Angielski](README.md) - [<ins>Polski</ins>](README.pl.md)

# Projekt Open Source: Track Expenses App Backend

Projekt tworzony przy współpracy społeczności "Bycza zagroda".
Projektem jest aplikacja do śledzenia wydatków osobistych. Aplikacja jest podzielona na
backend i frontend z osobnymi repozytoriami na GitHub. Aplikacja jest oparta na architekturze [REST](https://en.wikipedia.org/wiki/Representational_state_transfer)
i modelu [Minimum Viable Product (MVP)](https://en.wikipedia.org/wiki/Minimum_viable_product).

## Prerequisites:

Do uruchomienia aplikacji wymagana jest instalacja następujących narzędzi:
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) / [VSC](https://code.visualstudio.com/) / [Eclipse](https://www.eclipse.org/),
- [Java 17 LTS](https://openjdk.org/projects/jdk/17/),
- [MySql Workbench](https://www.mysql.com/products/workbench/) / [DBeaver](https://dbeaver.io/),
- [Git Bash](https://git-scm.com/downloads),
- [Maven 3.x](https://maven.apache.org/download.cgi),
- [Postman](https://www.postman.com/),
- [Docker](https://docs.docker.com/get-docker/) - Instalacja docker'a zalezy od systemu operacyjnego.
  Baza danych jest uruchamiana w kontenerze dockera. Nie jest to jednak obowiązkowe i do wersji 1.0 można 
  korzystać z bazy lokalnie
  bez docker'a

## Jak zacząć:
Należy sklonować repozytorium backend i frontend przez https lub ssh.

#### Repozytorium backend
- git clone: https://github.com/bycza-zagroda/track-expenses-app-backend.git

  lub

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
