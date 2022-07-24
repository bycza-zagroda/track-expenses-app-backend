[Angielski](README.md) - [<ins>Polski</ins>](README.pl.md)

[![License](https://img.shields.io/badge/Licencja-Apache%202.0-blue.svg)](doc/LICENSE)
[![](https://img.shields.io/github/workflow/status/bycza-zagroda/track-expenses-app-backend/run_tests.yml/develop)](https://github.com/bycza-zagroda/track-expenses-app-backend/actions)
<br>
[![Follow us!](https://img.shields.io/badge/FB-Bycza%20Zagroda-blue)](https://www.facebook.com/groups/byczazagroda/about/)
[![](https://img.shields.io/discord/787788929898250260?logoColor=%237289da)](https://discord.gg/5xmrnugs)

# track-expenses-backend

<p align="center">
  <img src="https://via.placeholder.com/150" alt="Track Expenses App Logo" />
</p>

* [O Projekcie](#o-projekcie)
* [Początkowe założenia aplikacji](#pocztkowe-zaoenia-aplikacji)
* [Wymagane aplikacje/narzędzia](#wymagane-aplikacje-narzedzia)
* [Jak zacząć](#jak-zaczacz)
  - [Repozytorium backend](#repozytorium-backend)
* [Stack technologiczny](#stack-technologiczny)
  - [Development](#development)
  - [Testy](#testy)
* [Kodeks postępowania współtwórców (CoC)](#kodeks-postpowania-wsptwrcw-coc)
* [Twój wkład](#twj-wkad)
* [Autorzy](#autorzy)
* [Licencja](#licencja)

## O Projekcie
Projekt tworzony przy współpracy społeczności "Bycza zagroda".
Projektem jest aplikacja do śledzenia wydatków osobistych. Aplikacja jest podzielona na
backend i frontend z osobnymi repozytoriami na GitHub. Aplikacja jest oparta na architekturze [REST](https://en.wikipedia.org/wiki/Representational_state_transfer)
i modelu [Minimum Viable Product (MVP)](https://en.wikipedia.org/wiki/Minimum_viable_product).

## Początkowe założenia aplikacji
- jest to mały projekt, aby można było go ukończyć z najlepszymi praktykami,
- zbudowanie rdzenia aplikacji, która wykonuje podstawowe operacje jak: dodaj, znajdź, zmodyfikuj, usuń wydatek/przychód,
- jest to projekt, w którym możemy wspólnie popracować i uczyć się od siebie nawzajem, poszerzać umiejętności lub
  też je utrwalać. Wszystko zależy od tego, na jakim etapie nauki Spring Boot 2 jesteście.

## Wymagane aplikacje/narzędzia
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

## Jak zacząć
Należy sklonować repozytorium backend i frontend przez https lub ssh.

### Repozytorium backend
- git clone: https://github.com/bycza-zagroda/track-expenses-app-backend.git

## Stack technologiczny

### Development
- [Java 17 LTS](https://openjdk.org/projects/jdk/17/)
- [Spring Boot 2](https://spring.io/projects/spring-boot)
- [Spring Data](https://spring.io/projects/spring-data)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger / OpenAPI](https://swagger.io/specification/)
- [MySql (docker)](https://www.mysql.com/)
- [Liquibase](https://www.liquibase.org/)
- [Maven 3.x](https://maven.apache.org/)

### Testy
- [JUnit5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Test Containers](https://www.testcontainers.org/)

## Kodeks postępowania współtwórców (CoC)
Więcej informacji znajdziesz w sekcji [Kodeks postępowania współtwórców](doc/CODE_OF_CONDUCT.pl.md)

## Twój wkład
Proszę przeczytać [CONTRIBUTING.md](doc/CONTRIBUTING.md)

## Autorzy
Społeczność [**_bycza-zagroda_**](https://github.com/orgs/bycza-zagroda/people) ❤

## Licencja
Projekt jest objęty licencją [_Apache License Version 2.0, January 2004_](doc/LICENSE)
