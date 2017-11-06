## 算法
> 算法是解决特定问题求解步骤的描述，在计算机中表现为指令的有限序列，并且每条指令表示一个或多个操作。

### 算法的特性
* 输入输出
* 有穷性
* 确定性
* 可行性

### 算法设计的要求
* 正确性
    * 算法程序没有语法错误
    * 算法程序对于合法的输入数据能够产生满足要求的输出结果
    * 算法程序对于非法的输入数据能够得出满足规格说明的结果
    * 算法程序对于精心选择的，甚至刁难的测试数据都有满足要求的输出结果
* 可读性
* 健壮性
* 时间效率高和存储量低

### 算法效率的度量方法
* 事后统计方法
* 事前分析估算方法

### 函数的渐进增长
 > 函数的渐进增长：给定两个函数f(n)和g(n)，如果存在一个整数N，使得对于所有的n>N，f(n)总是比g(n)大，那么，我们说f(n)的增长渐进快于g（n）。

 判断一个算法的效率时，函数中的常数和其他次要项常常可以忽略，而更应该关注主项（最高项）的阶数。

### 算法的时间复杂度
> 在进行算法分析时，语句总的执行次数T（n）是关于问题规模n的函数，进而分析T（n）随n的变化情况并确定T（n）的数量级。算法的时间复杂度，也就是算法的时间量度，记作：T(n)=O(f(n))。它表示随问题规模n的增大，算法执行时间的增长率和f(n)的增长率相同，称作算法的渐进时间复杂度，简称为时间复杂度。

O(1) < O(logn) < O(n) < O(nlogn) < O(n^2) < O(n^3) < (2^n) < O(n!) < O(n^n)

### 算法的空间复杂度
> 算法的空间复杂度通过计算算法所需的存储空间实现，算法空间复杂度的计算公式记作：S(n)=O(f(n))，其中，n为问题的规模，f(n)为语句关于n所占存储空间的函数。