# How we work with GitHub

## Starting work with the task.

1. Go to [our repository](https://github.com/bycza-zagroda/), select backend or frontend and create new fork.

![create fork](images/img001_create_fork.png)

You can leave displayed repository name.

---

2.Expand branches and click link "View all branches".

![view all branchesz](images/img002_switch_branch.png)

---

3. Click button "New branch" and fill form as below with the name of the branch for your task. Use pattern "feature/task-name", "bugfix/task-name".

![create branch](images/img003_create_branch.png)

Enter a name of new branch and select "develop" as a branch source.

## Local repository
1. Clone your repository

---

2. In local project directory execute commands: (--allow-empty allows us to execute commit without any changes in the project)

```shell
git commit -m "getting started" --allow-empty
git push
```

---

3. Back to your repository on GitHub, click "Pull requests" and "New pull request" button. 

![new pull request](images/img004_new_pull_request.png)

---

4. Select project repository as base repository and "develop" as a base branch. 
Head repository should point to your repository and field compare - new created branch.
Click "Create pull request" button.

![create draft pull request](images/img005_create_pull_request.png)

---

5. On the next page make sure that you have correctly set branches (develop in project repository, newly created in yours),
that you selected "Create draft pull request" and click on button "Draft pull request".

![create draft pull request](images/img006_draft_pull_request.png)

---

Now you can start working with the task. 

## Code review

Code review can be started from opened draft pull request. You can open it in several ways, below described one of it.
Go to project page and open "Pull requests" page. Find yours on the list and open it. Next, click the button "Ready for review". 

![ready for review](images/img007_ready_for_review.png)

## Completing work with the task.

After completing work with the task give an information to the leader that you ask for code review. 
Then Leader assign reviewer for your task and that you are waiting for checking. 
If there is some cases to improve, you need to improve them and give information to your reviewer that you finished. 
Next the leaders check that your changes can be merged to upstream.
