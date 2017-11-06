# 分布式文件系统HDFS

## HDFS是什么？

- 源自于Google的GFS论文
	- 发表于2003年10月
	- HDFS是GFS克隆版
	
- Hadoop Distributed File System
	- 易于扩展的分布式文件系统
	- 运行于大量普通廉价机器上，提供容错机制
	- 为大量用户提供性能不错的文件存取服务
	
## HDFS优点

- 高容错性
	- 数据自动保存多个复本
	- 副本丢失后，自动恢复

- 适合批处理
	- 移动计算而非数据
	- 数据位置暴露给计算框架

- 适合大数据处理
	- GB、TB、甚至PB级数据
	- 百万规模以上的文件数据
	- 10K+节点规模

- 流式文件访问
	- 一次性写入，多次读取
	- 保证数据一致性

- 可构建在廉价机器上
	- 通过多副本提高可靠性
	- 提供了容错和恢复机制
	
### HDFS缺点

- 低延迟数据访问
	- 不如毫秒级
	- 低延迟与高吞吐率

- 小文件存取
	- 占用NameNode大量内存
	- 寻道时间超过读取时间

- 并发写入、文件随即修改
	- 一个文件只能有一个写者
	- 仅支持append
	
## HDFS基础架构与原理

### 分布式文件系统的一种实现方式

![]()

### HDFS设计思想

![]()

### HDFS架构

![]()

- Active NameNode
	- 主Master（只有一个）
	- 管理HDFS的名称空间
	- 管理数据块映射信息
	- 配置副本策略
	- 处理客户端读写请求

- Standby NameNode
	- NameNode的热备
	- 定期合并fsimage和fsedits,推送给NameNode
	- 当Active NameNode出现故障时，快速切换为新的Active NameNode
	
- DataNode
	- Slave（有多个）
	- 存储实际的数据块
	- 执行数据块读/写

- Client
	- 文件切分
	- 与NameNode交互，获取文件位置信息
	- 与DataNode交互，读取或者写入数据
	- 管理HDFS
	- 访问HDFS
	
### HA与Federation

![]()

### 准备NameNode数据同步

![]()

### HDFS数据块（block）

- 文件被切分成固定大小的数据块

- 为何数据块如此之大

- 一个文件存储方式