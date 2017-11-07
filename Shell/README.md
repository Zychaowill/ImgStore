# Shell

- #!/usr/bin/env bash和#!/usr/bin/bash的比较

stackoverflow:
http://stackoverflow.com/questions/16365130/the-difference-between-usr-bin-env-bash-and-usr-bin-bash

通过/usr/bin/env 运行程序，用户不需要去寻找程序在系统中的位置（因为在不同的系统，命令或程序存放的位置可能不同），只要程序在你的$PATH中；

通过/usr/bin/env 运行程序另一个好处是，它会根据你的环境寻找并运行默认的版本，提供灵活性。
不好的地方是，有可能在一个多用户的系统中，别人在你的$PATH中放置了一个bash，可能出现错误。

大部分情况下，/usr/bin/env是优先选择的，因为它提供了灵活性，特别是你想在不同的版本下运行这个脚本；而指定具体位置的方式#! /usr/bin/bash，在某些情况下更安全，因为它限制了代码注入的可能。
