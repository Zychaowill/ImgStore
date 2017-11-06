# 分布式数据库HBase

## 0 用户画像系统

> 用户画像就是根据用户的各种行为抽象出的一个标签化的用户模型，简而言之，就是给用户贴标签

- 作用
	- 优化用户体验
		- 不仅对用户浏览体验优化，还包括产品消费过程的优化
	- 实现精准化营销
		- 显现用户的喜好跟需求属性，在跟用户点对点交互中，精准的匹配个性化的业务内容
	- 关联数据挖掘
		- 结合过去的画像数据及未来画像数据的变化，对用户做数据关联的挖掘
	- 个性化推荐
		- 为用户推送个性化的内容（比如今日头条、优酷）
		
- 技术挑战
	- 记录和存储亿级用户的画像
	- 支持和扩展不断增加的维度和偏好
	- 毫秒级的更新
	
- 我们需要一个这样的存储系统（MySQL, Oracle?）
	- 扩展性极强（行列扩展）
	- 动态更新
	
## 1 HBase概述

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/HBase%E6%A6%82%E8%BF%B0.bmp)

- HBase是一个构建在HDFS上的分布式列存储系统
- HBase是Apache Hadoop生态系统中的重要一员，主要用于海量结构化数据存储
- HBase是Google Bigtable的开源实现
- 从逻辑上将，HBase将数据按照表、行和列进行存储，它是一个分布式的、稀疏的、持久化存储的多维度排序表

- HBase VS HDFS
	- HDFS适合批处理场景
	- HDFS不支持数据随机查找
	- HDFS不支持数据更新
	
- HBase VS Hive
	- Hive适合批处理数据分析场景
	- Hive不适合实时的数据访问
	
- HBase特点
	- 良好的扩展性
	- 读和写的强一致性
	- 高可靠性
	- 与MapReduce良好的继承
	
- HBase应用场景
	- 网页库（360搜索 — 网络爬虫）
	- 商品库（淘宝搜索 — 历史账单查询）
	- 交易信息（淘宝数据魔方）
	- 云存储服务（小米）
	- 监控信息（OpenTSDB）
	
## 2 HBase数据模型

(Table, RowKey, Family, Qualifier, TimeStamp) -> Value

> 在HBase中，一行数据有行键(RowKey)作为键，包括多个列族(Family)，列族是由具有同时访问特性的多个列(Qualifier)组成的。数据是可以具有多版本的，由时间戳(TimeStamp)索引。

- A small example

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/A%20small%20example.bmp)

- 行键与表
	- 行键
		- 行键（row key）是数据行在表中的唯一标识
		- 所有操作都是基于主键的
	- 表
		- 表可以是稀疏的，空值不被存储
		- 表中的数据按照行键排序
		- 无模式:每行都有一个可排序的主键和任意多的列，列可以根据需要动态的增加，同一张表中不同的行可以有截然不同的列
		
- 特点
	- 大
		- 一个表可以有数十亿行，上百万列
	- 面向列
		- 面向列（族）的存储，列（族）独立检索
	- 稀疏
		- 对于空（null）的列，并不占用存储空间，表可以设计的非常稀疏
	- 数据多版本
		- 每个单元中的数据可以有多个版本
	- 数据类型单一
		- HBase中的数据都是字节，没有类型
		
- 列（族）式存储

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/%E5%88%97%E6%97%8F%E5%BC%8F%E5%AD%98%E5%82%A8.bmp)

## 3 HBase物理模型

### 3.1 概念模型

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/%E6%A6%82%E5%BF%B5%E6%A8%A1%E5%9E%8B.bmp)

### 3.2 物理模型

- 物理存储
	- 以列族为单位存储
	- 每个cell中会存储以下信息
		- Row Key
		- Column family name
		- Column name
		- Timestamp
		- Value
		
![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/%E7%89%A9%E7%90%86%E5%AD%98%E5%82%A8.bmp)

> Table中的所有行都按照row key的字典序排列

> Table在行的方向上分割为多个Region

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/%E6%8C%89%E7%85%A7RowKey%E5%AD%97%E5%85%B8%E5%BA%8F%E6%8E%92%E5%88%97.bmp)

> Region按大小分割的，每个表开始只有一个Region，随着数据增多，Region不断增大，当增大到一个阀值的时候，Region就会等分为两个新的Region，之后会有越来越多的Region

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/Region%E5%88%86%E5%89%B2.bmp)

> Region是HBase中分布式存储和负载均衡的最小单元，不同Region分布在不同RegionServer上

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/Region%E5%88%86%E5%B8%83%E5%9C%A8%E4%B8%8D%E5%90%8CRegionServer.bmp)

> Region虽然是分布式存储的最小单元，但并不是存储的最小单元。

> Region由一个或者多个Store组成，每个Store保存一个columns family

> 每个Store又有一个MemStore和0至多个StoreFile组成

> MemStore存储在内存中，StoreFile存储在HDFS上

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/Store.bmp)

## 4 HBase架构

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/HBase%E6%9E%B6%E6%9E%84.bmp)

### 4.1 HRegion（区域）

- HBase会自动地将表划分为不同的区域

- 每个区域包含所有行的一个子集

- 对用户来说，每个表是一堆数据的集合，靠主键来区分

- 从物理上来说，一张表被拆分成了多块，每一块是一个HRegion

- 我们用表名+开始和结束主键，来区分每一个HRegion

- 一个HRegion会保存一个表里面某段连续的数据，从开始主键到结束主键

- 一张完整的表格是保存在多个HRegion上面

### 4.2 HRegionServer

- 所有的数据库数据都保存在HDFS上面

- 用户通过访问HRegionServer获取这些数据

- 一台机器上面一般只运行一个HRegionServer

- 一个HRegionServer上面有多个HRegion，一个HRegion也只会被一个HRegionServer维护

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/HRegionServer.bmp)

- HRegionServer主要负责响应用户I/O请求，从HDFS读写数据，是HBase中最核心的模块

- HRegionServer内部管理了一系列HRegion对象

- 每个HRegion对应了Table中的一个Region，HRegion中由多个HStore组成

- 每个HStore对应了Table中的一个Column Family的存储

- 最好将具备共同IO特性的Column放在一个Column Family中

### 4.3 HMaster

- 每个HRegionServer都会与HMaster通信

- HMaster的主要任务就是给HRegionServer分配HRegion

- HMaster指定HRegionServer要维护哪些HRegion

- 当一台HRegionServer宕机时，HMaster会把它负责的HRegion标记为未分配，然后再把它们分配到其他HRegionServer中

> HMaster没有单点问题，HBase中可以启动多个HMaster，通过Zookeeper的Master Election机制保证总有一个HMaster运行，HMaster在功能上主要负责Table和Region的管理工作:

> 管理用户对Table的增、删、改、查操作

> 管理HRegionServer的负载均衡，调整Region分布

> 在Region Split后，负责新Region的分配

> 在HRegionServer停机后，负责失效HRegionServer上的Regions迁移
	
### 4.4 其他组件

- Client
	- HBase Client使用HBase的RPC机制与HMaster和HRegionServer进行通信
	- 对于管理类操作，Client与HMaster进行RPC
	- 对于数据读写类操作，Client与HRegionServer进行RPC

- Zookeeper
	Zookeeper中存储了META表的地址和HMaster的地址，HRegionServer也会把自己以Ephemeral方式注册到Zookeeper中，使得HMaster可以随时感知到各个HRegionServer的健康状态。此外，Zookeeper也避免了HMaster的单点问题。
	
### 4.5 安装HBase

#### 4.5.1 安装部署HBase

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/%E9%83%A8%E7%BD%B2HBase.bmp)

- 部署要点
	- HDFS
	- Zookeeper
	- master
	- RegionServer
	
- HBase Master界面

``` http://master:60010 ```	

- HBase HRegionServer界面

``` http://master:60030 ```

	
## 5 HBase Shell

### 5.1 HBase访问方式

- Native Java API
	- 最常规和高效的访问方式
	
- HBase Shell
	- HBase的命令行工具，最简单的接口，适合HBase管理使用

- Thrift Gateway
	- 利用Thrift序列化技术，支持C++, PHP, Python等多种语言，适合其他异构系统在线访问HBase表数据

- MapReduce
	- 直接使用MR作业处理HBase数据
	- 使用Pig/Hive处理HBase数据

### 5.2 HBase Shell

- 运行以下命令来启动HBase Shell（在此之前，保证Hadoop集群已经启动）

``` ./bin/hbase shell ```

- 建表: 表名scores, 有两个列族: 'grade'和'course'

``` create 'scores', 'grade', 'course' ```

- 查看HBase中的表

``` list ```

- 查看表结构

``` desc 'scores' ```

- put: 写入数据
	- 格式: ``` >put 't1', 'r1', 'c1', 'value', ts1 ```
		- t1: 表名
		- r1: 行键
		- c1: 列名
		- value: 值
		- ts1: 时间戳

``` 
	>put 'scores', 'Tom', 'grade', 5
	>put 'scores', 'Tom', 'course:math', 97
	>put 'scores', 'Tom', 'course:art', 87
	>put 'scores', 'Jim', 'grade', 4
	>put 'scores', 'Jim', 'course:match', 68
	>put 'scores', 'Jim', 'course:science', 89
```

- get: 随机查找数据
	- 格式
		
		```
			>get 't1', 'r1'
			>get 't1', 'r1', 'c1'
			>get 't1', 'r1', 'c1', 'c2'
			>get 't1', 'r1', {COLUMN=>'c1',TIMESTAMP=>ts1}
			>get 't1', 'r1', {COLUMN=>'c1',TIMESTAMP=>[ts1, ts2],VERSIONs=>4}
		```

```
	>get 'scores', 'Tom'
	>get 'scores', 'Tom', 'grade'
	>get 'scores', 'Tom', 'grade', 'course'
```
	
- scan: 范围查找数据
	- 格式
	
		```
			>scan 't1'
			>scan 't1', {COLUMNS=>'course:math'}
			>scan 't1', {COLUMNS=>['c1', 'c2'],LIMIT=>10,STARTROW=>'xyz'}
			>scan 't1', {REVERSED=>true}
		```
		
```
	>scan 'scores'
	>scan 'scores', {COLUMNS=>'course:math'}
	>scan 'scores', {COLUMNS=>'course'}
	>scan 'scores', {COLUMNS=>'course',LIMIT=>1,STARTROW=>'Jim'}
```
	
- delete: 删除数据
	- 格式
		
		```
			>delete 't1', 'r1', 'c1', ts1
		```
		
```
	>delete 'scores', 'Jim', 'course:math'
```		
	
- truncate: 删除全表数据

```
	>truncate 'scores'
```	
	
- alter: 修改表结构
	- 为scores表增加一个family列族，名为profile
	``` >alter 'scores', NAME=>'profile' ```	
	- 删除profile列族
	``` >alter 'scores', NAME=>'profile', METHOD=>'delete' ```

- 删除表的步骤
	- disabled 't1'
	- drop 't1'
	
```
	>disabled 'scores'
	>drop 'scores'
```
	
## 6 HBase Java API

- HBase使用Java语言编写的，支持Java编程是自然而然的事情

- 支持CRUD操作
	- Create
	- Read
	- Update
	- Delete
	
- Java API包含HBase Shell支持的所有功能，甚至更多

- Java API是访问HBase最快的方式

### 6.1 Java API程序设计步骤

- 1.创建一个Configuration对象
	- 包含各种配置信息
	
- 2.构建一个Connection连接
	- 提供Configuration对象

- 3.根据不同的功能来获得相应的角色
	- 管理: Admin
	- 访问数据: Table

- 4.执行相应的操作
	- 执行put、get、delete、scan等操作
	
- 5.关闭连接句柄
	- 释放各种资源

## 7 实践与企业案例

### 7.1 OpenTSDB背景

- 基于HBase构建的分布式、可伸缩的时间序列数据库（Time Series Database）

- 秒级数据采集所有metrics（指标信息），支持永久存储，可以做容量规划

- 可以从大规模的集群（包括集群中的网络设备、操作系统、应用程序）中获取相应的metrics并进行存储、所以以及服务

### 7.2 为什么传统数据库解决方案不行

- 时间序列数据特点
	- 数据量大
		- 10000台机器，每台机器50个指标，每5秒采样一次，则每秒平均产生（10000*50/5 = 100000）条数据
		- 每天产生3.6亿条数据
	- 访问模式简单
		- 给定指标名称，时间段，得到该时间段内所有数据

- 传统关系型数据库弊端
	- 无法存储海量数据
	- 检索海量数据的速度慢
	
### 7.3 OpenTSDB基本概念

- Metric: 监控项，比如CPU利用率

- Tags: 用来描述metric的标签，有tagk和tagv组成，即tagk = tagv

- Value: metric的实际值

- Timestamp: 时间戳，描述value对应的时间点

- Data Point: 即某个Metric在某个时间点的数值

e g:
```
	metric: proc.loadavg.1m
	timestamp: 1234567890
	value: 0.42
	tags: host=web42, pool=static
	
### 7.4 OpenTSDB可能的实现方案

- 简单的实现方案
	- rowKey=metric|timestamp|host=web42|pool=static
	- Column=v
	- Value=0.42
	
- 存在问题
	- rowKey太长，占用大量存储资源，且为字符串类型，查找开销大
	- 不易获得某个时间区间的所有值
	
### 7.5 OpenTSDB实现方案

- 使用HBase存储，核心的存储，使用两张表，tsdb和tsdb-uid

- tsdb-uid保存元数据信息，即编码信息
	- metric—>3字节整数、tagk—>3字节整数、tagv—>3字节整数
	- proc.loadavg.1m—>052、host—>001、web42—>028、pool—>047、static—>001
	
- tsdb保存实际数据
	- Rowkey: 跟前一种方案类似，但是做了编码
	- Row: 每行存储一个小时的数据，列名是秒偏移量，列值是metric值
	
#### 7.5.1 表tsdb-uid设计

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/tsdb-uid%E8%AE%BE%E8%AE%A1.bmp)

#### 7.5.2 表tsdb设计

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/tsdb%E8%AE%BE%E8%AE%A1.bmp)

#### 7.5.3 OpenTSDB架构

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HBase/OpenTSDB%E6%9E%B6%E6%9E%84.bmp)

- Server: OpenTSDB的agent，通过Collector收集数据，推送数据

- TSD: 是对外通信的无状态的服务器，对数据进行汇总和存取

- HBase: TSD收到数据后，通过异步客户端AsyncHbase将数据写入到HBase

### 7.6 HBase在淘宝的应用:历史订单查询

- 历史账单
	- 查询方式简单: 列出用户给定时间段内的消费记录
	- 三个月内的账单: 查询频繁，数据量较小
	- 三个月前的账单: 查询频率低，数据量大
	
- 解决方案
	- 三个月内的账单数据: 放到MySQL中
	- 三个月前的账单数据: 放到HBase中
		- 行键设计: userid + time + id
		- 列: 购物的明细，包括商品名、商品描述，价格等

### 7.7 HBase在搜索引擎中的引用:网络爬虫

> 爬虫程序爬去的是整个互联网上的所有或特定主题的数据，这个数据量一般是PB级，因此用使用分布式的爬去设计和分布式存储是架构设计的不二选择，基于Hadoop的HBase可以实现数据存储的目的。

- HBase作用
	- 存储爬取网页的历史信息，包括URL、爬取时间、优先级等
	- 网页去重
	- 网页信息的实时更新
	
- HBase表设计
	- Row Key值: URL
	- 列: 爬虫抓取到的信息都可以存放在同一列族info中，主要的字段有:
		- oriUrl: 原始URL值，主机部分是域名
		- statusCode: 访问URL时的返回的状态码
		- linkNum: 该链接被其他链接引用的次数
		- type: 页面类型
		- Title: 页面标题
		- Length: 页面大小
		- Lastmodified: 最后修改时间

## Reference Link

- [HBase存储格式](http://www.oschina.net/p/hbase/?fromerr=aeo6LzCq)

- [HBase在淘宝的应用和优化小结](http://blog.nosqlfan.com/html/3694.html)
