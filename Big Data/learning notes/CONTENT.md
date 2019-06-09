## 大数据学习路线

### day01

- 01.基础部分课程介绍
- 02.linux系统安装过程
- 03.linux图形界面及文件系统结构介绍
- 04.局域网工作机制和网络地址配置
- 05.vmware虚拟网络的配置介绍
- 06.linux网络配置及CRT远程连接
- 07.回顾上午的ip地址配置
- 08.文件常用操作命令
- 09.文件权限的操作
- 10.常用系统操作命令
- 11.ssh免密登陆配置

### day02

- 01-11

### day03

- 01.关于yum网络版仓库和scp命令缺失的问题
- 02.自动化部署脚本
- 03.zookeeper的功能和应用场景
- 04.zookeeper的集群角色分配原理
- 05.zookeeper集群的搭建
- 06.zookeeper的命令行客户端及znode数据结构类型监听等功能
- 07.zookeeper集群自动启动脚本及export变量作用域的解析
- 08.zookeeper的java客户端api（1-1）
- 08.zookeeper的java客户端api（1-2）
- 09.zookeeper的java客户端api（2）
- 10.分布式应用系统服务器上下线动态感知程序开发（1-1）
- 10.分布式应用系统服务器上下线动态感知程序开发（1-2）
- 11.zookeeper客户端线程的属性-守护线程
- 12.分布式应用系统程序效果测试

### day04

- 01.关于zk客户端连接失败的问题
- 02.内容大纲介绍
- 03.线程实现的两种方式
- 04.synchronized同步代码块示例
- 05.ReentrantLock的方法示例
- 06.lock和synchronized的一些区别和选择考虑因素
- 07.java并发包中的线程池种类及特性介绍
- 08.并发包中各种线程池的用法及future获取任务返回结果的机制
- 09.BlockingQueue的功能和使用示例
- 10.volatile的工作机制代码测试
- 11.关于并发编程的一些总结
- 12.activemq（1-1）
- 12.activemq（1-2）
- 13.activemq
- 14.java的反射实现api
- 15.动态代理的工作机制
- 16.动态代理的demo代码
- 17.利用socket来进行远程过程调用

### day05

- 01.关于socket流阻塞的含义和wait-notify的用法（1-1）
- 01.关于socket流阻塞的含义和wait-notify的用法（1-2）
- 02.自定义rpc框架的设计思路
- 02.自定义rpc框架的设计思路
- 03.nio的原理和示例代码（1-1）
- 03.nio的原理和示例代码（1-2）
- 03.nio的原理和示例代码（1-3）
- 04.高性能nio框架netty
- 05.spring的初始化机制及自定义注解的实现方式
- 06.rpc框架的服务端设计思路（1-1）
- 06.rpc框架的服务端设计思路（1-2）
- 07.rpc服务端的完整实现流程
- 08.rpc框架爱的客户端设计思路
- 09.rpc框架的联调测试
- 10.jvm入门（1-1，2，3，4）

### day06

- 01.hadoop生态圈介绍及就业前景
- 02.hadoop在实际项目中的架构分析
- 03.hadoop安装环境准备
- 04.hadoop集群安装
- 05.hadoop shell命令操作
- 06.使用JavaAPI操作HDFS文件系统

### day07

- 01.客户端向HDFS写数据的流程
- 02.namenode管理源数据的机制
- 03.hdfs的java客户端
- 04.hadoop中的rpc框架
- 05.shell脚本定时采集日志数据到hdfs

### day08

- 01.复习 & mapreduce的核心思想
- 02.wordcount程序原理及代码实现
- 03.wordcount程序运行流程分析
- 04.流量汇总程序的开发 & 关于人物切片和对应maptask并行度的概念介绍（1-1）
- 04.流量汇总程序的开发，如何实现hadoop的序列化接口（1-2）
- 05.客户端提交job流程之源码跟踪
- 06.客户端提交job的流程梳理和总结 & 自定义partition编程
- 07.自定义partitioner编程实现数据自定义分区处理

### day09

- 01.复习 & 流量汇总排序的mr实现
- 02.MR内部的shuffle过程详解
- 03.combiner的运行机制及代码实现
- 04.MR运行在yarn集群流程分析 & 本地模式调试MR程序
- 05.用java -jar的方式运行mr程序
- 06.yarnrunner源码实现从eclipse中运行mr & 用mr实现join逻辑
- 07.用mapreduce来实现join逻辑

### day10

- 01.复习 & 解决数据倾斜的思路分析
- 02.map端join实现 & 倒排索引实现
- 03.找出QQ共同好友的实现
- 04.使用groupingcomparator求同一订单中最大金额的订单（待处理）
- 05.运营商流量日志增强-自定义outputformat
- 06.自定义inputformat示例
- 07.常用mr配置参数

### day11

- 01.HA机制以及设计思路的分析
- 02.HA配置文件讲解
- 03.HA集群搭建
- 04.HA联邦机制 & hive的实现机制
- 05.Hive安装 & 体验
- 06.hive的使用 & 分区表概念和示范

### day12

- 01.hive的分桶作用
- 02.hive中的sql讲解，重点是join操作
- 03.一个广告推送用户画像项目介绍
- 04.hive自定义函数 & transform的使用
- 05.hive中的一个较难的面试题
- 06.flume的简单使用 & 作业题

### day13

- 01.hive复习 & flume使用
- 02.flume多个agent连接 & azkaban介绍
- 03.azkaban示例演示
- 04.azkaban提交各种示例 & sqoop安装
- 04.azkaban执行hdfs-hive-mr等类型job示例
- 05.sqoop使用简单示例
- 05.sqoop导入导出
- 06.点击流数据项目背景分析
- 06.sqoop导入导出各种示例
- 07.点击流数据分析项目背景介绍
- 08.点击六数据分析转化路径概念---数据预处理

### day14

- 01~06.项目讲解

### day15

- 01-04.项目讲解
- 05.贝叶斯算法 & KNN算法思想讲解
- 06.机器学习算法思想 —— kmeans

### day16

- 01.HBase简介
- 02.HBase安装以及常用shell命令
- 03.javaapi操作HBase（1）
- 04.javaapi操作HBase（2）
- 05.原理——1
- 05.原理——2
- 06.mr操作HBase

### day17

- x道云笔记项目1~4

### day18

- 01.课程介绍 & 实时计算的应用场景
- 02.storm核心组件和架构
- 03.storm集群部署&任务提交部署讲解
- 04.storm wordcount案例分析 & 代码编写
- 05.storm wordcount流程分析

### day19

- 01.storm集群任务提交流程
- 01.storm提问环节
- 01.storm是什么
- 02.storm集群任务提交流程
- 03.storm内部通信机制1
- 04.storm内部通信机制2
- 05.storm

### day20

- 01.
