# Spark推荐系统

## 1 推荐系统简介
### 1.1 什么是推荐系统

### 1.2 推荐系统任务

### 1.3 推荐系统原理

- 好友

- 历史兴趣

- 注册信息

## 2 推荐平台基础架构

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.12.38.png)

### 2.1 推荐平台关键模块

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.16.28.png)

### 2.2 实时推荐系统

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.19.49.png)

### 2.3 推荐平台基础架构改进:lambda architecture

> 实际场景也可以在Spark Streaming中调用机器学习库

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.21.42.png)

### 2.4 实时推荐模块

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.24.08.png)

## 3 书籍推荐系统

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.27.11.png)

### 3.1 整体架构

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.29.15.png)

### 3.2 离线训练模块

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.32.37.png)

#### 3.2.1 数据加载

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.37.10.png)

#### 3.2.2 模型训练示例

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.39.19.png)

### 3.3 实时训练模块

#### 3.3.1 模型训练示例

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.41.46.png)

#### 3.3.2 更新用户标签

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.42.35.png)

## 4 Summary

![](https://github.com/Zychaowill/ImgStore/blob/master/hadoop/屏幕快照%202017-11-03%20下午8.44.11.png)
