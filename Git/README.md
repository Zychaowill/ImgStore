# Git conflict

If the command 'git pull origin master' generates a conflict, solution as follows:
git-br-feature1

For this case, Git can't execute 'fast merge' and only try to merge their own modifications, but the merge may conflict.

Let' try to merge feature1 to master. When we execute the command 'git merge feature1', Some information will show up.
```bash
$ git merge feature1
Auto-merging readme.txt
CONFLICT (content): Merge conflict in readme.txt
Automatic merge failed; fix conflicts and then commit the result.
```


For this, we can use 'git status' to view conflict files.
```bash
$ git status
On branch master
Your branch is ahead of 'origin/master' by 2 commits.

Unmerged paths:
(use "git add/rm <file>..." as appropriate to mark resolution)

both modified: readme.txt

no changes added to commit (use "git add" and/or "git commit -a")
```


We view directly the conflict file 'readme.txt'.
```bash
Git is a distributed version control system.
Git is free software distributed under the GPL.
Git has a mutable index called stage.
Git tracks changes of files.
<<<<<<< HEAD Creating a new branch is quick & simple. 
======= 
Creating a new branch is quick AND simple. 
>>>>>>> 
feature1
```


Git will label different content of branches by '<<<<<<<', '=======' and '>>>>>>>', then we do modify:
```bash
Git is a distributed version control system.
Git is free software distributed under the GPL.
Git has a mutable index called stage.
Git tracks changes of files.
Creating a new branch is quick AND simple.
```


Next, we can sumit our files.
```bash
$ git add readme.txt 
$ git commit -m "conflict fixed"
[master 59bc1cb] conflict fixed
```

After some operations, the relationship of master and feature1 as follows:
git-br-conflict-merged

Of course, we can use 'git log' command to view the merge result of branches.
Command as: git log --graph --pretty=oneline --abbrev-commit
At last, we will delete feature1 branch.
Finish work.

## Summary
1#  When Git can't merge branches,  only to resolve all conflicts, then submit all files. Finish as last.

2#  'git log --graph' command can view branch merge graph.