# What's the big data

## 大数据应用领域

### 互联网领域

- 搜索引擎

- 推荐系统

- 广告系统

### 电信领域

- 用户画像

- 用户行为分析

### 医药生物领域

- DNA分析

### 视频领域

- 视频存储

- 视频分析

### 金融领域

- 信用卡欺诈分析

- 用户分析

### 矿产勘探领域

- 矿产石油勘查预测

## 大数据技术框架

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/%E5%A4%A7%E6%95%B0%E6%8D%AE%E6%8A%80%E6%9C%AF%E6%A1%86%E6%9E%B6.bmp)

## Hadoop生态系统
### Hadoop特点

- 源代码开源（免费）

- 社区活跃、参与者众多

- 涉及分布式存储和计算的方方面面

- 已得到企业界验证

### hadoop生态系统

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/Hadoop%E7%94%9F%E6%80%81%E7%B3%BB%E7%BB%9F.bmp)

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/Hadoop%E7%94%9F%E6%80%81%E7%B3%BB%E7%BB%9F2.bmp)

### Hadoop构成:Flume（非结构化数据收集）

- Cloudera开源的日志收集系统

- 用于非结构化数据收集

- Flume特点
	- 分布式
	- 高可靠性
	- 高容错性
	- 易于定制与扩展

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/Flume.bmp)

### Hadoop构成:Sqoop（结构化数据收集）

- Sqoop: SQL-to-Hadoop

- 连接传统关系型数据库和Hadoop的桥梁
	- 把关系型数据库的输入导入到Hadoop系统（如HDFS、HBase和Hive）中
	- 把数据从Hadoop系统里抽取并导出到关系型数据库里
	
- 利用MapReduce加快数据传输速度

- 批处理方式进行数据传输

### Hadoop的构成:HDFS(分布式文件系统)

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
	
### Hadoop构成:YARN（资源管理器）

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

### Hadoop构成:MapReduce（分布式计算框架）

- 源自于Google的MapReduce论文
	- 发表于2004年12月
	- Hadoop MapReduce是Google MapReduce克隆版
	
- MapReduce特点
	- 良好的扩展性
	- 高容错性
	- 适合PB级以上海量数据的离线处理
	
![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/MR.bmp)

### Hadoop构成:Hive（基于MR的数据仓库）

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
	
## Spark生态系统

