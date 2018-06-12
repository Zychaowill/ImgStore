Spark运行模式：

- local
单机运行，通常用于测试

将Spark应用以多线程方式，直接运行在本地，便于调试。
本地模式分类：
local
local[k]
local[*]: 启动跟CPU数目相同的executor

- standalone（独立模式）
独立运行在一个集群中

- YARN/mesos
运行在资源管理系统上，比如YARN或者mesos
Spark On YARN存在两种模式
    - yarn-client
    - yarn-cluster


