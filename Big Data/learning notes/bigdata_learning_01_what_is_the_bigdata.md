# What's the big data

## 1 大数据应用领域

### 1.1 互联网领域

- 搜索引擎

- 推荐系统

- 广告系统

### 1.2 电信领域

- 用户画像

- 用户行为分析

### 1.3 医药生物领域

- DNA分析

### 1.4 视频领域

- 视频存储

- 视频分析

### 1.5 金融领域

- 信用卡欺诈分析

- 用户分析

### 1.6 矿产勘探领域

- 矿产石油勘查预测

## 2 大数据技术框架

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/%E5%A4%A7%E6%95%B0%E6%8D%AE%E6%8A%80%E6%9C%AF%E6%A1%86%E6%9E%B6.bmp)

## 3 Hadoop生态系统
### 3.1 Hadoop特点

- 源代码开源（免费）

- 社区活跃、参与者众多

- 涉及分布式存储和计算的方方面面

- 已得到企业界验证

### 3.2 hadoop生态系统

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/Hadoop%E7%94%9F%E6%80%81%E7%B3%BB%E7%BB%9F.bmp)

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/Hadoop%E7%94%9F%E6%80%81%E7%B3%BB%E7%BB%9F2.bmp)

### 3.3 Hadoop构成:Flume（非结构化数据收集）

- Cloudera开源的日志收集系统

- 用于非结构化数据收集

- Flume特点
	- 分布式
	- 高可靠性
	- 高容错性
	- 易于定制与扩展

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/Flume.bmp)

### 3.4 Hadoop构成:Sqoop（结构化数据收集）

- Sqoop: SQL-to-Hadoop

- 连接传统关系型数据库和Hadoop的桥梁
	- 把关系型数据库的输入导入到Hadoop系统（如HDFS、HBase和Hive）中
	- 把数据从Hadoop系统里抽取并导出到关系型数据库里
	
- 利用MapReduce加快数据传输速度

- 批处理方式进行数据传输

### 3.5 Hadoop的构成:HDFS(分布式文件系统)

- 源自于Google的GFS论文
	- 发表于2003年10月
	- HDFS是GFS克隆版
	
- HDFS特点
	- 良好的扩展性
	- 高容错性
	- 适合PB级以上海量数据的存储
	
- 基本原理
	- 将文件切分成等大的数据块，存储到多台机器上
	- 将数据切分、容错、负载均衡等功能透明化
	- 可将HDFS看成一个容量巨大、具有高容错性的磁盘
	
- 应用场景
	- 海量数据的可靠性存储
	- 数据归档
	
### 3.6 Hadoop构成:YARN（资源管理器）

- YARN是什么
	- Hadoop 2.0新增系统
	- 负责集群的资源管理和调度
	- 使得多种计算框架可以运行在一个集群中
	
- YARN的特点
	- 良好的扩展性、高可用性
	- 对多种类型的应用程序进行统一管理和调度
	- 自带了多种多用户调度器，适合共享集群环境
	
![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/YARN.bmp)

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/YARN2.bmp)

### 3.7 Hadoop构成:MapReduce（分布式计算框架）

- 源自于Google的MapReduce论文
	- 发表于2004年12月
	- Hadoop MapReduce是Google MapReduce克隆版
	
- MapReduce特点
	- 良好的扩展性
	- 高容错性
	- 适合PB级以上海量数据的离线处理
	
![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/MR.bmp)

### 3.8 Hadoop构成:Hive（基于MR的数据仓库）

- 由Facebook开源，最初用于解决海量结构化的日志数据统计问题:
	- ETL(Extraction-Transformation-Loading)工具
	
- 构建在Hadoop之上的数据仓库
	- 数据计算使用MR，数据存储使用HDFS
	
- Hive定义了一种类SQL查询语句—HQL
	- 类似SQL，但不完全相同
	
- 通常用于进行离线数据处理（采用MapReduce）

- 可认为是一个HQL<——>MR的语言翻译器

- 日志分析
	- 统计网站的一个时间段内的pv、uv

- 多维度数据分析

- 大部分互联网公司使用Hive进行日志分析，包括百度、淘宝等

- 其他场景
	- 海量结构化数据离线分析
	- 低成本进行数据分析（不直接编写MR）
	
## 4 Spark生态系统

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/Spark.bmp)

### 4.1 Hadoop版本衍化:Apache Hadoop

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/Hadoop%E7%89%88%E6%9C%AC%E8%A1%8D%E5%8C%96.bmp)

### 4.2 Hadoop版本选择

- 不同发行版兼容性
	- 架构、部署和使用方法一致，不同之处仅在若干内部实现
	
- 建议选择公司发行版，比如CDH或HDP
	- 类比原生Linux与Ubuntu/Red Hat的关系
	- 更易维护和升级
	- 经过集成测试，不会面临版本兼容问题
	
### 4.3 Hadoop集群搭建

- 全人工搭建

- 自动化安装软件
	- Cloudera Manager
	- Ambari
	
#### 4.3.1 Hadoop全人工模式安装:常见错误

- 防火墙未关闭（所有节点都要关闭）
	- Connection Refused
	
- 配置文件抄错
	- core-site.xml,yarn-site.xml,hdfs-site.xml以及mapred-site.xml
	
- 多次格式化HDFS
	- 每次格式化后，均会导致HDFS启动失败，解决方案：清空HDFS的各个数据目录，然后重启HDFS
	- 格式化HDFS是非常危险的，会导致所有数据丢失！
	
### 4.4 Hadoop运行模式

- 本地模式
	- 一个节点，不会启动任何服务
	
- 伪分布式模式
	- 一个节点，所有服务均运行在该节点上
	
- 分布式模式
	- 多于一个节点
	
## 5 大数据挑战

- 机器数目多
	- 从几百个，到上千个甚至上万
	- 如何只用几个人管理如此大规模的机器？
	
- 数据量大
	- PB级数据量，数据增速极快
	
- 软件栈复杂
	- 涉及到众多的系统，这些系统会交织在一起
	- 管理，监控和升级这些软件的工作量很大
	
- 定位问题困难
	- 问题来源可能是，硬件，操作系统，大数据系统等
	
## 6 Summary

- 大数据技术体系

- Hadoop生态系统

- Hadoop版本衍化及其版本选择

- 大数据挑战

## 补充

#### 大数据技术栈
	
- Centos 7

- Java 1.8

- Scala 2.11.x(不能是2.10或2.12)

- Bash Shell

- Hadoop 2.7.3

- Hive 2.1.1

- HBase 1.2.4

- Flume 1.7.0

- Sqoop 1.99.7

- Presto 0.166

- Spark 2.1.0

- Kafka 0.9.0

- Zookeeper 3.4.9

#### 集成开发环境

- IntelliJ IDEA

#### 项目构建（包管理、编译、发布）

- Maven