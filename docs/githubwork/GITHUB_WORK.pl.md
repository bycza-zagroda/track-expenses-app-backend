[Angielski](GITHUB_WORK.md) - [<ins>Polski</ins>](GITHUB_WORK.pl.md)

# Jak pracujemy z GitHub'em

* [Rozpoczęcie pracy nad zadaniem](#rozpoczecie-pracy-nad-zadaniem)
  - [Gita](#gita)
  - [IntelliJ IDEA](#intellij-idea)
* [Code review](#code-review)
* [Ukończenie zadania](#ukoczenie-zadania)

## Rozpoczęcie pracy nad zadaniem

1. Przejdź do [strony z repozytorium](https://github.com/bycza-zagroda/track-expenses-app-backend) i wybierz **fork**

![create fork](images/img001_create_fork.png)

---

2. Rozwiń listę dostępnych branchy i wybierz **View all branches**

![view all branches](images/img002_switch_branch.png)

---

3. Stwórz nowy branch o nazwie odpowiadającej poniższemu nazewnictwu ↓

- **_feature/task-name_** - dla zadań dodających nowe funkcjonalności
- **_bugfix/task-name_** - dla zadań, które naprawiają błędy

![create branch](images/img003_create_branch.png)

⚠ Upewnij się że wybierasz gałąź **_develop_** jak źródłową

## Lokalne repozytorium

1. Sklonuj repozytorium używając

### Gita

`git clone https://github.com/bycza-zagroda/track-expenses-app-backend.git`

### IntelliJ IDEA

![IntelliJ Import Step 1](images/img008_intellij_import_step_1.png)
![IntelliJ Import Step 2](images/img008_intellij_import_step_2.png)
![IntelliJ Import Step 3](images/img008_intellij_import_step_3.png)


---

2. W cmd lub powershellu wykonaj poniższą komendę: (flag `--allow-empty` [pozwala na commitowanie bez zmian])

```shell
git commit -m "getting started" --allow-empty
git push
```

---

3. Wróć na [główną stronę projektu na GitHubie](https://github.com/bycza-zagroda/track-expenses-app-backend) i wybierz **_Pull requests_** -> **_New pull request_**.

![new pull request](images/img004_new_pull_request.png)

---

4. Wybierz nowo utworzoną gałąź jako źródłową np. **_feature/task-1_** oraz 
**_develop_** jako gałąź docelową i kliknij **_Create pull request_**.

![create draft pull request](images/img005_create_pull_request.png)

5. 

![create draft pull request](images/img006_draft_pull_request.png)

---

Od teraz możesz zacząć pracę nad zadaniem!

## Code review

Code Review zaczynamy od otworzenia **_draft pull request_** i oznaczając je jako **_Ready for review_**

![ready for review](images/img007_ready_for_review.png)

## Ukończenie zadania

Po sprawdzeniu twoich kodów przez lidera zespołu i/lub innych developerów, twój kod będzie zmergowany przez lidera.
