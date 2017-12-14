# 日志收集模块

## 1 背景介绍

### 1.1 常见数据来源

- 非结构化数据

- 结构化数据

### 1.2 数据收集与入库要求



## 2 利用Flume完成日志手机

### 2.1 Flume是什么

- 由Cloudera公司开源

- 分布式、可靠、高可用的海量日志采集系统

- 数据源可定制，可扩展

- 数据存储系统可定制，可扩展

- 中间件：屏蔽了数据源和数据存储系统的异构性

### 2.2 Flume NG基本架构

![]()

- 可串联，避免小文件的产生

### 2.3 Agent之Source: 概述

- Source负责接收event或通过特殊机制产生event，并将events批量的放到一个或多个Channel

- 包含event驱动和轮询2种类型

- 不同类型的Source：
  - 与系统集成的Source: Syslog, Netcat
  - 自动生成事件的Source: Exec
  - 监听文件夹下文件变化: Spooling Directory Source, Taildir Source
  - 

## 3 结构化数据全量和增量导入

## 4 日志收集模块设计与实现
