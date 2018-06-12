Spark运行模式：

- local
单机运行，通常用于测试

将Spark应用以多线程方式，直接运行在本地，便于调试。<br/>
本地模式分类：<br/>
local<br/>
local[k]<br/>
local[*]: 启动跟CPU数目相同的executor<br/>

- standalone（独立模式）
独立运行在一个集群中

- YARN/mesos
运行在资源管理系统上，比如YARN或者mesos<br/>
Spark On YARN存在两种模式
    - yarn-client（driver在客户端）
    - yarn-cluster（driver在node节点）


