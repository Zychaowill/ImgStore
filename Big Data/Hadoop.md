# 基于Hadoop的数据仓库

## 1 什么是数据仓库

> 数据仓库是面向主题的、集成的、具有时间特征的、稳定的数据集合，用以支持经营管理中的决策制定过程

- 典型应用:
	- 报表生成
	- 数据分析
	- 数据挖掘
	
- 数据仓库其他特征
	- 数据量非常大（TB以上）
	- 是数据库的一种新型应用
	- 使用人员较少
	
- 商用数据仓库
	- 典型代表: db2, teradata, vertica
	- 价格昂贵，支持数据量通常TB或以下
	
- 大数据时代数据仓库
	- 数据量非常大
	- 扩展性和容错性很重要
	- 成本考量	

## 2 基于Hadoop数据仓库的基本架构

- 技术手段
	- 通常使用Hive作为数据仓库
		- 超大数据集设计的计算扩展能力
		- 支持HQL查询 — 简单，学习代价低
		- 统一的元数据管理
		
- 基本特点
	- 支持海量数据
	- 多维数据分析
	- 使用人员较少
	- 数据延迟较高
	
### 2.1 基于Hadoop的数据仓库:第一版

![](https://github.com/buildupchao/ImgStore/blob/master/hadoop/%E5%9F%BA%E4%BA%8EHadoop%E7%9A%84%E6%95%B0%E6%8D%AE%E4%BB%93%E5%BA%931.bmp)		

- 优点
	- 满足了数据仓库的基本要求
	- 能够处理海量数据
	- 系统扩展性和容错性极好
	
- 缺点
	- 性能较低，实时性不好
	
### 2.2 基于Hadoop的数据仓库:第二版

![](https://github.com/buildupchao/ImgStore/blob/master/hadoop/%E5%9F%BA%E4%BA%8EHadoop%E7%9A%84%E6%95%B0%E6%8D%AE%E4%BB%93%E5%BA%932.bmp)

- 改进
	- 使用MPP(Presto)系统提高查询性能
	
- 优点
	- 满足了数据仓库的基本要求
	- 能够处理海量数据
	- 系统扩展性和容错性极好
	- 实时性较好
	
- 缺点
	- 数据延迟高（数据从产生到入库，再到查询，整个周期长）
	
### 2.3 基于Hadoop的数据仓库:第三版（增加实时pipeline）

![](https://github.com/buildupchao/ImgStore/blob/master/hadoop/%E5%9F%BA%E4%BA%8EHadoop%E7%9A%84%E6%95%B0%E6%8D%AE%E4%BB%93%E5%BA%933.bmp)

- 改进
	- 使用Spark Streaming系统降低数据延迟

- 优点
	- 满足了数据仓库的基本要求
	- 能够处理海量数据
	- 系统扩展性和容错性极好
	- 实时性较好
	- 数据延迟低

## 3 数据仓库具体实例

> 网站报表系统

- 基本作用
	- 按照业务要求生成报表
	- 报表可实时产生或按天产生
	
- 数据规模
	- 数据量: TB级
	- 表数目: 100+
	
- 用户量
	- 约几十个
	
### 3.1 收集数据

![](https://github.com/buildupchao/ImgStore/blob/master/hadoop/Step1.bmp)

### 3.2 ETL

![](https://github.com/buildupchao/ImgStore/blob/master/hadoop/Step2new.bmp)

- ETL
	- Extract, Transform, Load
	- 可使用MapReduce/Spark/Pig实现
	- 存储格式: 行式存储与列式存储
	
- 行存储与列存储

![](https://github.com/buildupchao/ImgStore/blob/master/hadoop/%E8%A1%8C%E5%BC%8F%E5%AD%98%E5%82%A8%E4%B8%8E%E5%88%97%E5%BC%8F%E5%AD%98%E5%82%A8.bmp)

> 如何创建带压缩的ORC表

- ETL后日志格式（文本格式）如下:

![](https://github.com/buildupchao/ImgStore/blob/master/hadoop/ETL%E5%90%8E%E6%97%A5%E5%BF%97%E6%A0%BC%E5%BC%8F.bmp)	
	
- 临时表（文本格式）定义如下:
``` 	
	CREATE EXTERNAL TABLE tmp_logs (
		domain_id INT,
		log_time STRING,
		log_date STRING,
		log_type INT,
		uin BIGINT
	)
	ROW FORMAT DELIMITED
	FIELDS TERMINATED BY ','
	STORED AS TEXTFILE
	LOCATION '/user/hivetest/logs'; 
```
	
- 将数据导入临时表tmp_logs:
``` LOAD DATA INPATH '/nginx/logs/2016011206' OVERWRITE INTO TABLE tmp_logs; ```

- 将临时表中数据导入到orc格式的表中:
``` 
	CREATE TABLE logs (
		domain_id INT,
		log_time STRING,
		log_date STRING,
		log_type INT,
		uin BIGINT
	)
	PARTITION BY(log_time STRING)
	STORED AS ORC
	tblproperties("orc.compress"="SNAPPY");
	
	INSERT INTO TABLE logs PARTITION(dt='2016-01-12-06') SELECT * FROM tmp_logs;
 ```

- 压缩算法

![](https://github.com/buildupchao/ImgStore/blob/master/hadoop/%E5%8E%8B%E7%BC%A9%E7%AE%97%E6%B3%95.bmp)

- 查询
``` 
	SELECT domain_id, sum(log_type) FROM logs WHERE 
	log_time>'2016-01-12-06' GROUP BY domain_id;  
```
	
### 3.3 参数化报表与可视化

![](https://github.com/buildupchao/ImgStore/blob/master/hadoop/Step3.bmp)

- 参数化报表
	- 根据用户定制的数据要求，生成SQL

- 可视化工具
	- Echarts: [http://echarts.baidu.com/](http://echarts.baidu.com/)
	- D3.js: [https://d3js.org/](https://d3js.org/)
	- Tableau: 商用可视化软件

## 4 Summary

- 基于Hadoop构建数据仓库的好处
	- 开源免费
	- 支持海量数据
	- 周边工具成熟
	
- 基于Hadoop构建数据仓库的流程
	- 数据收集
	- 数据ETL
	- 参数化报表与可视化