每道题后的*代表此题在面试中出现的频率，即重要程度。

一. hadoop里有几类节点分别是什么？**
namenode,datanode,journalnode(同步active,standby的editlog的)

二. hive可以支持几种查询引擎？**
3种
mapreduce,spark,tez

三. hbase如何创建预分区，预分区有什么好处。****
预分区可以减少数据量猛增时region分裂造成的资源消耗。
CREATE 'user', 'info', SPLITS => ['20170101000000000', '20170115000000000', '20170201000000000']
region0                   - 20170101000000000
region1 20170101000000000 - 20170115000000000
region2 20170115000000000 - 20170201000000000
region3 20170201000000000 -
(每个region都有一个startKey和一个endKey来表示该region存储的rowKey范围)
在创建Hbase表的时候默认一张表只有一个region，所有的put操作都会往这一个region中填充数据，当这个region过大时就会进行split。
如果在创建HBase表的时候就进行预分区,会减少当数据量猛增时由于region split带来的资源消耗。

四. 例举出几种Storm中的stream grouping的类型。***
1. Shuffle Grouping - 随机分组(随机派发stream里面的tuple，保证每个bolt接收到的tuple数目大致相同)
2. Fields Grouping  - 字段分组(指定某个字段作为分组条件，那么字段值相同的tuple会被分到同一个Blot的task进行处理)
3. All Grouping - 全部分组(就是对于每一个tuple，所有的bolt都会收到)
4. None Grouping - 无分组(这种分组方法和第一种shuffle Grouping效果是一样的)
5. Global Grouping - 全局分组(spout/bolt发射的所有tuple全部进入下游bolt的同一个task，通常选择下游bolt中id最小的task)
6. Direct Grouping - 直接分组(允许spout/bolt决定其发射出的任一tuple由下游bolt的哪个task接收并处理)

五. Spark RDD的操作分为几类 ****
1. 输入操作 input
textFile,parallelize
2. 转换操作 transformation
map,flatMap,union,partitionBy,combineByKey,reduceByKey,groupByKey
3. 行动操作 action
count,collect,foreach
4. 控制操作 control
cache,checkpoint

================================================================================================================================

一. Hive调优 **
#Join优化
1.表的大小从左边到右边依次增大
2.标志机制/*+streamtable(table_name)*/(显示的告知查询优化器哪张表示大表)

#开启并行计算
set hive.exec.parallel=true;

#Map-side聚合(Combiner)
set hive.map.aggr=true;

二. Yarn调优 ***
#Yarn 集群参数调优
1.CPU配置
yarn.app.mapreduce.am.resource.cpu-vcores // ApplicationMaster虚拟CPU内核(建议设置成1)
yarn.nodemanager.resource.cpu-vcores // 容器虚拟CPU内核(建议cpu全部内核数都给他-默认值8)

2.内存配置
yarn.nodemanager.resource.memory-mb // 容器内存(24G)
mapreduce.map.memory.mb // Map任务内存(1G)
mapreduce.reduce.memory.mb // Reduce任务内存(1G)
yarn.scheduler.minimum-allocation-mb // 最小容器内存(1G)
yarn.scheduler.maximum-allocation-mb // 最大容器内存(24G)
yarn.scheduler.increment-allocation-mb // 容器内存增量(512M)

3.同一个Map或者Reduce并行执行(如果有较慢的任务，会在别的机器上启动一个相同的map或reduce重复执行，谁成功了就把落后的kill掉)
mapreduce.map.speculative // Map任务推理执行(默认是不勾选的，建议勾选)
mapreduce.reduce.speculative // Reduce任务推理执行(默认是不勾选的，建议勾选)

4.JVM重用
mapreduce.job.ubertask.enable // 启用Ubertask优化(默认是不勾选的，建议勾选)
mapreduce.job.ubertask.maxmaps // Ubertask最大Map -> 超过多少个map启用jvm重用
mapreduce.job.ubertask.maxreduces // Ubertask最大Reduce -> 超过多少Reduce启用jvm重用
mapreduce.job.ubertask.maxbytes // Ubertask最大作业大小 -> application的输入大小的阀值，默认为block大小

三. storm调优 *
#并行度调优
work数量
executor:5 task:10
rebalance动态调优
#work内存
在Strom的配置文件storm.yaml中设置worker的启动参数 worker.childopts: "-Xmx2048m"

四. Spark调优（谭老师文档上有详细介绍）****
1.参数调优
num-executors (50-100)
executor-memory (4G-8G)
executor-cores (2-4)
2. 防止不必要的jar包上传和分发
spark.yarn.jar=hdfs://...
3. 存储格式(列式存储)
Orc Parquet
4. 运行在高配置的机器上
NodeLabel: 打标签
5. 对Rdd进行缓存
rdd.cache
6. 优化符操作
reduceByKey 代替 groupByKey
7. 使用Kryo优化序列化
conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
conf.registerKryoClasses(Array(classOf[MyClass1], classOf[MyClass2]))
8. 作业并行化
作业放在单独的线程中

五. Hbase调优 **
1. AutoFlush
将HTable的setAutoFlush设为false，可以支持客户端批量更新。即当Put填满客户端flush缓存时，才发送到服务端。默认是true。
2. Scan Attribute Selection
scan时建议指定需要的Column Family，减少通信量，否则scan操作默认会返回整个row的所有数据（所有Coulmn Family）
3. 数据块缓存
关闭缓存:列族的缓存默认是打开的，如果经常顺序访问或很少被访问，可以关闭列族的缓存。
激进缓存:可以选择一个列族赋予更高的优先级缓存。
4. 协处理器
修改hbase-site.xml
<property>
  <name>hbase.coprocessor.user.region.classes</name>
  <value>org.apache.hadoop.hbase.coprocessor.AggregateImplementation</value>
</property>
hbase shell
1、disable指定表。
hbase> disable 'stu'
2、添加aggregation 
hbase>alter 'stu', METHOD => 'table_att','coprocessor'=>'|org.apache.hadoop.hbase.coprocessor.AggregateImplementation||'
3、重启指定表 
hbase> enable 'stu'

六. Impala调优 *
1. SQL优化，使用之前调用执行计划
2. parquet进行存储
3. 避免产生很多小文件（如果有其他程序产生的小文件，可以使用中间表）
4. 使用合适的分区技术，根据分区粒度测算
5. 使用compute stats进行表信息搜集
6. 输出文件时，避免使用美化输出
7. 使用profile输出底层信息计划，在做相应环境优化

七. Implal为什么比Hive快 *
1.Java前端与C++处理后端，效率要高于单独使用JAVA。
2.基于内存进行计算，速度上优于Hive中使用MapReduce使用磁盘来计算。
3.支持Parquet列式存储（这一点Hive近期也支持了）
4.直接使用C++访问HDFS里的文件，绕过了原本的JAVA接口。
5.使用Code Generation（代码生成）对CPU指令进行优化。

八. Kudu批处理调优 * STORED AS KUDU 支持update
Impala写入Kudu不支持pstmt.addBatch()的操作，要提高写入速度需要自己实现Sql语句的批处理即：
INSERT INTO table VALUES (1,value1,value2,value3),(2,value1,value2,value3),(3,value1,value2,value3)

================================================================================================================================

一. HDFS工作原理-写入数据 ****
1.客户端通过调用DistributedFileSystem的create方法创建新文件。
2.DistributedFileSystem通过RPC调用namenode去创建一个新文件，创建前，namenode会做各种校验，比如文件是否存在，客户端有无权限等。
3.前两步结束后，会返回FSDataOutputStream的对象，客户端开始写数据到DFSOutputStream，把数据切成一个个小的packet，然后排成队列data quene。
4.DataStreamer会去处理接受data quene，如果副本数是3，那么就找到3个datanode，把他们排成一个pipeline。
DataStreamer把packet按队列输出到管道的第一个datanode中，第一个datanode又把packet输出到第二个datanode中，以此类推。
5.DFSOutputStream还有一个对列叫ack quene，也是由packet组成，等待datanode的收到响应，当pipeline中的所有datanode都表示已经收到的时候，
这时akc quene才会把对应的packet包移除掉。
6.客户端完成写数据后调用close方法关闭写入流。
7.收到最后一个ack后，通知datanode把文件标视为已完成。

源码分析:
客户端需要获得FSDataOutputStream流,需要调用FileSystem.create方法,这里面又调用了DistributedFileSystem.create方法,接着又调用了
DFSClient.create的方法,DFSClient可以连接到Hadoop的文件系统，它里面有一个字段是ClientProtocol(和NameNode进程沟通和连接,读/写数据块),
而ClientProtocol是通过一个代理类ProxyAndInfo创建的,
创建的时候使用了java.net.InetSocketAddress(如果是主机名+端口号将尝试解析主机名)它继承了SocketAddress。

二. HDFS工作原理-读取数据 **
1.首先调用FileSystem对象的open方法，其实是一个DistributedFileSystem的实例。
2.DistributedFileSystem通过rpc获得文件的block的locations。
3.前两步会返回一个FSDataInputStream对象，客户端调用read方法，连接离客户端最近的datanode并连接。
4.数据从datanode源源不断的流向客户端。
5.如果第一块的数据读完了，就会关闭指向第一块的datanode连接，接着读取下一块。
6.如果第一批block都读完了，会去namenode拿下一批block的locations，然后继续读，如果所有的块都读完，这时就会关闭掉所有的流。

源码分析:
客户端需要获得FSDataInputStream流,需要调用FileSystem.open方法,这里面又调用了DistributedFileSystem.open方法,接着又调用了DFSClient.open的方法,
之后跟写类似是通过DFSClient和ClientProtocol连接服务端的。

三. Yarn工作原理 **
1. client调用job.waitForCompletion方法，向整个集群提交MapReduce作业。
2. 新的作业ID由资源管理器分配。
3. 将作业的资源(包括Jar包, 配置文件, split信息)拷贝给HDFS。
4. 通过调用资源管理器的submitApplication()来提交作业。
5. 当资源管理器收到submitApplciation()的请求时, 就将该请求发给调度器(scheduler), 调度器分配container。
6. MRAppMaster通过创造一些bookkeeping对象来监控作业的进度, 得到任务的进度和完成报告。
7. 然后其通过分布式文件系统得到由客户端计算好的输入split。
8. 如果作业很小, 应用管理器会选择在其自己的JVM中运行任务。如果不是小作业, 那么应用管理器向资源管理器请求container来
运行所有的map和reduce任务。
9. 当一个任务由资源管理器的调度器分配给一个container后, 应用管理器通过联系节点管理器来启动container。 
10. 任务由一个主类为YarnChild的Java应用执行. 在运行任务之前首先本地化任务需要的资源, 比如作业配置, JAR文件, 以及分布式缓存的所有文件。 
11. 最后, 运行map或reduce任务。
12. 除了向应用管理器请求作业进度外, 客户端每5分钟都会通过调用waitForCompletion()来检查作业是否完成。

四. Spark On Yarn原理(yarn-client 董老师课件) ****
编写完一个程序提交到Yarn上去，先从Driver提交给ResourceManager,ResourceManager会找一个节点，启动ApplicationMaster,
ApplicationMaster启动后会和ResourceManager通信，给Executor申请资源，申请好资源后会跟NodeManager通信，
启动你的Executor，Executor启动后跟Driver通信，领取Task。
ApplicationMaster的作用是申请资源，如果Executor挂了，他会跟ResourceManager通信再申请一个Executor，启动在某个节点上，
如果这个节点挂了，他知道有2个Executor丢了，他会跟ResourceManager通信在其他节点上启动两个Executor，Executor启动后
会跟跟Driver通信，领取Driver调度给他的Task。

五. 如何解决Spark数据倾斜 *****
1. 进行两阶段聚合，第一次是局部聚合，先给每个key都打上一个随机 接着对打上随机数后的数据，执行reduceByKey等聚合操作，进行局部聚合，
然后将各个key的前缀给去掉，再次进行全局聚合操作，就可以得到最终结果了。
2. 如果是join操作则需要使用附加了随机前缀的RDD与另一个膨胀n倍的RDD进行join。

六. Spark任务调度源码 **
1. val conf = new SparkConf().setAppName(this.getClass.getSimpleName()).setMaster("local")
set("spark.app.name", name)
set("spark.master", master)

2. val sc = new SparkContext(conf)
1. 调用SparkContext的createTaskScheduler方法创建了TaskSchedulerImpl(task(任务)调度器),并调用它的initialize方法,
根据schedulingMode(spark.scheduler.mode)的值来创建schedulableBuilder为FIFOSchedulableBuilder(先进先出调度模式)或
FairSchedulableBuilder(公平调度模式)
2. 创建了DAGScheduler(stage(阶段)调度器) 执行了eventProcessLoop.start()

3. val data = sc.parallelize(arr)
返回ParallelCollectionRDD

4. val log = sc.textFile("logs/wordcount.log")
返回HadoopRDD

5. val result = log.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
flatMap: 返回MapPartitionsRDD
map: 返回MapPartitionsRDD
reduceByKey: 如果分区相同返回MapPartitionsRDD,否则就返回ShuffledRDD

6. result.foreach(println(_))
1. 调用了SparkContext的runJob方法,runJob里又调用了dagScheduler.runJob->submitJob->将作业封装成JobSubmitted事件放入
LinkedBlockingDeque-双端链阻塞队列等待作业执行。
2. 由于创建SparkContext的时候调用了eventProcessLoop.start方法获取LinkedBlockingDeque中的作业事件(阻塞)，调用了onReceive->doOnReceive->
handleJobSubmitted(完成job到stage的转换)->submitStage(计算stage之间的依赖关系并做出处理)->submitMissingTasks(将stage拆分成task并生成
TaskSet,并提交并提交到TaskScheduler)->taskScheduler.submitTasks(接受TaskSet，创建TaskSetManager并加入到schedulableBuilder中执行任务)

==================================================================================================================================

一. 多线程同步 *
1. 同步方法
public synchronized void save1(int money)
2. 同步代码块
public void save2(int money) {
	synchronized (this) {}
}
3. 特殊域变量
private volatile int account = 100;
4. 重入锁ReentrantLock
private Lock lock = new ReentrantLock();
lock.lock();
lock.unlock();
5. 使用ThreadLocal管理变量
private ThreadLocal<Integer> account2 = new ThreadLocal<Integer>(){
	@Override
	protected Integer initialValue(){
		return 100;
	}
};
initialValue() get() set()
6. 阻塞队列
private LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
7. 原子变量
private AtomicInteger account = new AtomicInteger(100);
account.addAndGet(money);