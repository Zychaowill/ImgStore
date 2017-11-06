# 分布式计算引擎MapReduce

## 1 MR背景及应用场景

### 1.1 MR的定义

- 源自于Google的MapReduce论文
	- 发表于2004年12月
	- Hadoop MapReduce是Google MapReduce克隆版
	
- MapReduce特点
	- 易于编程
	- 良好的扩展性
	- 高容错性
	- 适合PB级以上海量数据的离线处理

### 1.2 常见MR应用场景

- 简单的数据统计，比如网站pv、uv统计

- 搜索引擎建索引

- 海量数据查找

- 复杂数据分析算法实现
	- 聚类算法
	- 分类算法
	- 推荐算法
	- 图算法
	
### 1.3 MapReduce的特色 — 不擅长的方面

- 实时计算
	- 像MySQL一样，在毫秒级或者秒级内返回结果

- 流式计算
	- MR的输入数据集是静态的，不能动态变化
	- MR自身的设计特点决定了数据源必须是静态的
	
- DAG计算
	- 多个应用程序存在依赖关系，后一个应用程序的输入为前一个的输出

## 2 MR编程模型

### 2.1 MR的实例 — WordCount

- 场景: 有大量文件，里面存储了单词，且一个单词占一行

- 任务: 如何统计每个单词出现的次数？

- 类似应用场景
	- 搜索引擎中，统计最流行的K个搜索词
	- 统计搜索词频率，帮助优化搜索词提示
	
#### 2.1.1 Case

- Case 1: 整个文件可以加载到内存中
	- sort datafile | uniq -c
	
- Case 2: 文件太大不能加载到内存中，但<word, count>可以存放到内存中

- Case 3: 文件太大无法加载到内存中，且<word, count>也不行

#### 2.1.2 Solution

- 将问题泛化: 有一批文件（规模为TB级或者PB级），如何统计这些文件中所有的单词出现的次数

- Solution 1: 用一个单独的程序去读取和统计文件中所有的单词出现次数

- Solution 2: 启动多个程序分别读取不同文件中的单词出现次数，然后讲这些结果进行合并

#### 2.1.3 Why need Shuffling & Reducing?

![]()

#### 2.1.4 Thought line

- Input: 一系列key/value对

- 用户提供两个函数实现
	- map(k, v) -> k1, v1
	- reduce(k1, list(v1)) -> k2, v2
	
- (k1, v1)是中间key/vaue结果对

- Output: 一系列(k2, v2)对

![]()

### 2.2 MR解决哪些问题?

- 任务的拆分
	- 将大的任务拆解为小的任务
	
- 任务的执行
	- 并行调度和执行这些小的任务
	
- 结果的聚合
	- 将各个小人物的结果汇聚
	
#### 2.2.3 MR编程模型

- MR将作业的整个运行过程分为两个阶段:Map阶段和Reduce阶段

- Map阶段由一定数量的Map Task组成
	- 输入数据格式解析: InputFormat
	- 输入数据处理: Mapper
	- 数据分组: Partitioner
	
- Reduce阶段由一定数量的Reduce Task组成
	- 数据远程拷贝
	- 数据按照key排序
	- 数据处理: Reducer
	- 数据输出格式: OutputFormat

![]()

#### 2.2.4 InputFormat

#### 2.2.5 Split与Block

#### 2.2.6 Combiner

#### 2.2.7 Partitioner

### 2.3 Summary

- Map阶段
	- InputFormat(默认TextInputFormat)
	- Mapper
	- Combiner (local reducer)
	- Partitioner
	
- Reduce阶段
	- Reducer
	- OutputFormat(默认TextOutputFormat)

## 3 MR架构及核心设计机制

## 4 MR编程

## 5 MR应用案例