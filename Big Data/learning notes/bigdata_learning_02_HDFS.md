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

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/%E5%88%86%E5%B8%83%E5%BC%8F%E6%96%87%E4%BB%B6%E7%B3%BB%E7%BB%9F%E4%B8%80%E7%A7%8D%E5%AE%9E%E7%8E%B0%E6%96%B9%E5%BC%8F.bmp)

### HDFS设计思想

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HDFS%E8%AE%BE%E8%AE%A1%E6%80%9D%E6%83%B3.bmp)

### HDFS架构

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HDFS%E6%9E%B6%E6%9E%84.bmp)

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

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HA_Federation.bmp)

### 准备NameNode数据同步

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/%E4%B8%BB%E5%A4%87NN%E6%95%B0%E6%8D%AE%E5%90%8C%E6%AD%A5.bmp)

### HDFS数据块（block）

- 文件被切分成固定大小的数据块
	- 默认数据块大小为128MB，可配置
	- 若文件大小不到128MB，则单独存成一个block
	
- 为何数据块如此之大
	- 数据传输时间超过寻道时间（高吞吐率）

- 一个文件存储方式
	- 按大小被切分成若干个block，存储到不同节点上
	- 默认情况下每个block有三个副本
	
### 为什么选择三副本？

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/%E4%B8%BA%E4%BB%80%E4%B9%88%E9%80%89%E6%8B%A9%E4%B8%89%E5%89%AF%E6%9C%AC.bmp)

### HDFS内部机制

- 写流程

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HDFS%20Write.bmp)

- 读流程

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HDFS%20Read.bmp)

- 物理拓扑

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/%E7%89%A9%E7%90%86%E6%8B%93%E6%89%91.bmp)

- 副本存放策略

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/%E5%89%AF%E6%9C%AC%E5%AD%98%E6%94%BE%E7%AD%96%E7%95%A5.bmp)

- 可靠性策略

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/%E5%8F%AF%E9%9D%A0%E6%80%A7%E7%AD%96%E7%95%A5.bmp)

### HDFS不适合存储小文件

- 元信息存储在NN内存中
	- 一个节点的内存是有限的
	
- 存取大量小文件消耗大量的寻道时间
	- 类比拷贝大量小文件与拷贝同等大小的一个大文件
	
- NN存储block数目是有限的
	- 一个block元信息消耗大约150 byte内存
	- 存储1亿个block，大约需要20GB内存
	- 如果一个文件大小为10K，则一亿个文件大小仅为1TB（但要消耗掉NN 20GB内存）
	
### 如何动态增加新的DN？

- 将其他DN上的hadoop安装包（里面的配置文件已经修改好了）拷贝到新节点相同目录下
	- 确保配置文件是经过修改的，跟其他DN一致
	
- 在新节点上
	- 解压安装包
	- 启动DN: hadoop-deamon.sh start datanode
	
## HDFS程序设计方法

### HDFS Shell命令

#### 文件操作命令

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HDFS%20Shell.bmp)

- 将本地文件上传到HDFS上
	- bin/hdfs dfs -copyFromLocal /local/data /hdfs/data
	- bin/hdfs dfs -put /local/data /hdfs/data
	
- 删除文件/目录
	- bin/hdfs dfs -rmr /hdfs/data
	
- 创建目录
	- bin/hdfs dfs -mkdir /hdfs/data
	
#### 管理命令

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/HDFS%20Shell%202.bmp)

#### 管理脚本

- 在sbin目录下
	- start-all.sh
	- start-dfs.sh
	- start-yarn.sh
	- hadoop-deamon(s).sh
	
- 单独启动某个服务
	- hadoop-deamon.sh start namenode
	- hadoop-deamon.sh start datanode
	
#### You know?

- bin/hdfs和bin/hadoop区别（一样的，建议使用hdfs，hadoop是很早引入的）
	- bin/hdfs dfs -ls /tmp
	- bin/hadoop dfs -ls /tmp
	
- 不同目录表示的却别
	- bin/hdfs dfs -ls /tmp
	- bin/hdfs dfs -ls file:///tmp
	- bin/hdsf dfs -ls hdfs://master:9000/tmp
	
- 查看文件或目录的数据块存放位置
	- hdfs fsck /test/data -locations -blocks -racks -files
	
### HDFS WEB UI使用

- 打开HDFS WEB UI
	- Host:port, port为50070
	
- 观察WEB UI中的信息
	- DataNode个数
	- 总的文件数目，block数目
	- HDFS版本信息
	
## Reference Link

- ![HDFS存储原理分析](http://www.cnblogs.com/raphael5200/p/5497218.html)