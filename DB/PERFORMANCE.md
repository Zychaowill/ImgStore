### 慢查询优化基本步骤

```
0.先运行看看是否真的很慢，注意设置SQL_NO_CACHE
1.where条件单表查，锁定最小返回记录表。这句话的意思是把查询语句的where都应用到表中返回的记录数最小的表开始查起，单表每个字段分别查询，看哪个字段的区别度最高。
2.explain查看执行计划，是否与1预期一致（从锁定记录较少的表开始查询）
3.order by limit 形式的sql语句让排序的表优先查
4.了解业务方使用场景
5.加索引时参照建索引的几大原则
6.观察结果，不符合预期继续从0分析
```

- 1.复杂语句写法

Origin SQL Script: 53 data, 1.87S
<br/>
```SQL
select
   distinct cert.emp_id 
from
   cm_log cl 
inner join
   (
      select
         emp.id as emp_id,
         emp_cert.id as cert_id 
      from
         employee emp 
      left join
         emp_certificate emp_cert 
            on emp.id = emp_cert.emp_id 
      where
         emp.is_deleted=0
   ) cert 
      on (
         cl.ref_table='Employee' 
         and cl.ref_oid= cert.emp_id
      ) 
      or (
         cl.ref_table='EmpCertificate' 
         and cl.ref_oid= cert.cert_id
      ) 
where
   cl.last_upd_date >='2013-11-07 15:03:00' 
   and cl.last_upd_date<='2013-11-08 16:00:00';
```

explain:<br/>
```
+----+-------------+------------+-------+---------------------------------+-----------------------+---------+-------------------+-------+--------------------------------+
| id | select_type | table      | type  | possible_keys                   | key                   | key_len | ref               | rows  | Extra                          |
+----+-------------+------------+-------+---------------------------------+-----------------------+---------+-------------------+-------+--------------------------------+
|  1 | PRIMARY     | cl         | range | cm_log_cls_id,idx_last_upd_date | idx_last_upd_date     | 8       | NULL              |   379 | Using where; Using temporary   |
|  1 | PRIMARY     | <derived2> | ALL   | NULL                            | NULL                  | NULL    | NULL              | 63727 | Using where; Using join buffer |
|  2 | DERIVED     | emp        | ALL   | NULL                            | NULL                  | NULL    | NULL              | 13317 | Using where                    |
|  2 | DERIVED     | emp_cert   | ref   | emp_certificate_empid           | emp_certificate_empid | 4       | meituanorg.emp.id |     1 | Using index                    |
+----+-------------+------------+-------+---------------------------------+-----------------------+---------+-------------------+-------+--------------------------------+
```

SQL Script after optimization: 10ms
<br/>
```SQL
select
	cert.emp_id
from
	cm_log cl
inner join
	employee emp
	on cl.ref_table='Employee'
	and cl.ref_old=emp.id
where
	cl.last_upd_date >= '2013-11-07 15:03:00'
	and cl.last_upd_date <= '2013-11-08 16:00:00'
	and emp.is_deleted = 0
union
select
	cert.emp_id
from
	cm_log cl
inner join
	emp_certificate emp_cert
	on cl.ref_table='EmpCertificate'
	and cl.ref_old=emp_cert.emp_id
where
	cl.last_upd_date >= '2013-11-07 15:03:00'
	and cl.last_upd_date <= '2013-11-08 16:00:00'
	and emp.is_deleted = 0
```

explain:<br/>
```
+----+--------------+------------+--------+---------------------------------+-------------------+---------+-----------------------+------+-------------+
| id | select_type  | table      | type   | possible_keys                   | key               | key_len | ref                   | rows | Extra       |
+----+--------------+------------+--------+---------------------------------+-------------------+---------+-----------------------+------+-------------+
|  1 | PRIMARY      | cl         | range  | cm_log_cls_id,idx_last_upd_date | idx_last_upd_date | 8       | NULL                  |  379 | Using where |
|  1 | PRIMARY      | emp        | eq_ref | PRIMARY                         | PRIMARY           | 4       | meituanorg.cl.ref_oid |    1 | Using where |
|  2 | UNION        | cl         | range  | cm_log_cls_id,idx_last_upd_date | idx_last_upd_date | 8       | NULL                  |  379 | Using where |
|  2 | UNION        | ec         | eq_ref | PRIMARY,emp_certificate_empid   | PRIMARY           | 4       | meituanorg.cl.ref_oid |    1 |             |
|  2 | UNION        | emp        | eq_ref | PRIMARY                         | PRIMARY           | 4       | meituanorg.ec.emp_id  |    1 | Using where |
| NULL | UNION RESULT | <union1,2> | ALL    | NULL                            | NULL              | NULL    | NULL                  | NULL |             |
+----+--------------+------------+--------+---------------------------------+-------------------+---------+-----------------------+------+-------------+
53 rows in set (0.01 sec)
```

- 2.明确应用场景

Origin SQL Script: 951 data, 6.22S
```SQL
select
	*
from
	stage_poi sp
where 
	sp.accurate_result=1
	and (
		sp.sync_status=0
		or sp.sync_status=2
		or sp.sync_status=4
	);
```

explain:<br/>
```
+----+-------------+-------+------+---------------+------+---------+------+---------+-------------+
| id | select_type | table | type | possible_keys | key  | key_len | ref  | rows    | Extra       |
+----+-------------+-------+------+---------------+------+---------+------+---------+-------------+
|  1 | SIMPLE      | sp    | ALL  | NULL          | NULL | NULL    | NULL | 3613155 | Using where |
+----+-------------+-------+------+---------------+------+---------+------+---------+-------------+
```

让explain的rows尽量逼近951<br/>
```SQL
select count(*), accurate_result from stage_poi group by accurate_result;
+----------+-----------------+
| count(*) | accurate_result |
+----------+-----------------+
|     1023 |              -1 |
|  2114655 |               0 |
|   972815 |               1 |
+----------+-----------------+

select count(*), sync_status from stage_poi group by sync_status;
+----------+-------------+
| count(*) | sync_status |
+----------+-------------+
|     3080 |           0 |
|  3085413 |           3 |
+----------+-------------+
```

找业务方去沟通，看看使用场景。业务方是这么来使用这个SQL语句的，每隔五分钟会扫描符合条件的数据，处理完成后把sync_status这个字段变成1,五分钟符合条件的记录数并不会太多，1000个左右。了解了业务方的使用场景后，优化这个SQL就变得简单了，因为业务方保证了数据的不平衡，如果加上索引可以过滤掉绝大部分不需要的数据
<br/><br/>
根据建立索引规则，使用如下语句建立索引<br/>
```SQL
alter table stage_poi add index idx_acc_status(accurate_result, sync_status);
```
<br/><br/>
SQL Script after optimization: 200ms
```
952 rows in set (0.20 sec)
```

> 我们再来回顾一下分析问题的过程，单表查询相对来说比较好优化，大部分时候只需要把where条件里面的字段依照规则加上索引就好，如果只是这种“无脑”优化的话，显然一些区分度非常低的列，不应该加索引的列也会被加上索引，这样会对插入、更新性能造成严重的影响，同时也有可能影响其它的查询语句。所以我们第4步调查SQL的使用场景非常关键，我们只有知道这个业务场景，才能更好地辅助我们更好的分析和优化查询语句。

<br/><br/>

- 3.无法优化的语句

Origin SQL Script: 10 data, 13 sec<br/>
```SQL
select
	c.id,
	c.name,
	c.position,
	c.sex,
	c.phone,
	c.office_phone,
	c.feature_info,
	c.birthday,
	c.creator_id,
	c.is_keyperson,
	c.giveup_reason,
	c.status,
	c.data_source,
	from_unixtime(c.created_time) as created_time,
	from_unixtime(c.last_modified) as last_modified,
	c.last_modified_user_id
from
	contact c
inner join
	contact_branch cb
		on c.id = cb.contact_id
inner join
	branch_user bu
		on cb.branch_id = bu.branch_id
		and bu.status in (
			1, 2
		)
inner join
	org_emp_info oei
		on oei.data_id = bu.user_id
		and oei.node_left >= 2875
		and oei.node_right <= 10802
		and oei.org_category = -1
order by
	c.created_time desc
limit 0, 10;
```

explain:<br/>
```
+----+-------------+-------+--------+-------------------------------------+-------------------------+---------+--------------------------+------+----------------------------------------------+
| id | select_type | table | type   | possible_keys                       | key                     | key_len | ref                      | rows | Extra                                        |
+----+-------------+-------+--------+-------------------------------------+-------------------------+---------+--------------------------+------+----------------------------------------------+
|  1 | SIMPLE      | oei   | ref    | idx_category_left_right,idx_data_id | idx_category_left_right | 5       | const                    | 8849 | Using where; Using temporary; Using filesort |
|  1 | SIMPLE      | bu    | ref    | PRIMARY,idx_userid_status           | idx_userid_status       | 4       | meituancrm.oei.data_id   |   76 | Using where; Using index                     |
|  1 | SIMPLE      | cb    | ref    | idx_branch_id,idx_contact_branch_id | idx_branch_id           | 4       | meituancrm.bu.branch_id  |    1 |                                              |
|  1 | SIMPLE      | c     | eq_ref | PRIMARY                             | PRIMARY                 | 108     | meituancrm.cb.contact_id |    1 |                                              |
+----+-------------+-------+--------+-------------------------------------+-------------------------+---------+--------------------------+------+----------------------------------------------+
```

去掉order by 和 limit<br/>
```
+----------+
| count(*) |
+----------+
|   778878 |
+----------+
1 row in set (5.19 sec)
```
排序之前居然锁定了778878条记录，如果针对70万的结果集排序，将是灾难性的，怪不得这么慢，那我们能不能换个思路，先根据contact的created_time排序，再来join会不会比较快呢？<br/>
修改SQL，也可以用straight_join来优化：<br/>
```SQL
select
	c.id,
	c.name,
	c.position,
	c.sex,
	c.phone,
	c.office_phone,
	c.feature_info,
	c.birthday,
	c.creator_id,
	c.is_keyperson,
	c.giveup_reason,
	c.status,
	c.data_source,
	from_unixtime(c.created_time) as created_time,
	from_unixtime(c.last_modified) as last_modified,
	c.last_modified_user_id
from
	contact c
where
	exists (
		select
			1
		from 
			contact_branch cb
		inner join
			branch_user bu
			on cb.branch_id = bu.branch_id
			and bu.status in (
				1, 2
			)
		inner join
			org_emp_info oei
			on oei.data_id = bu.user_id
			and oei.node_left >= 2875
			and oei.node_right <= 10802
			and oei.org_category = -1
		where
			c.id = cb.contact_id
	)
order by
	c.created_time desc
limit 0, 10;

10 rows in sec (0.00 sec)
```

本以为至此大工告成，但我们在前面的分析中漏了一个细节，先排序再join和先join再排序理论上开销是一样的，为何提升这么多是因为有一个limit！<br/>
大致执行过程是：mysql先按索引排序得到前10条记录，然后再去join过滤，当发现不够10条的时候，再次取10条，再次join，这显然在内层join过滤的数据非常多的时候，将是灾难的，极端情况，内层一条数据都找不到，mysql还傻乎乎的每次取10条，几乎遍历了这个数据表！<br/>
<br/>
用不同的参数校验:<br/>
```SQL
select
   sql_no_cache   c.id,
   c.name,
   c.position,
   c.sex,
   c.phone,
   c.office_phone,
   c.feature_info,
   c.birthday,
   c.creator_id,
   c.is_keyperson,
   c.giveup_reason,
   c.status,
   c.data_source,
   from_unixtime(c.created_time) as created_time,
   from_unixtime(c.last_modified) as last_modified,
   c.last_modified_user_id    
from
   contact c   
where
   exists (
      select
         1        
      from
         contact_branch cb         
      inner join
         branch_user bu                     
            on  cb.branch_id = bu.branch_id                     
            and bu.status in (
               1,
            2)                
         inner join
            org_emp_info oei                           
               on  oei.data_id = bu.user_id                           
               and oei.node_left >= 2875                           
               and oei.node_right <= 2875                           
               and oei.org_category = - 1                
         where
            c.id = cb.contact_id           
      )        
   order by
      c.created_time desc  limit 0 ,
      10;
      
Empty set (2 min 18.99 sec)
```
由于mysql的neted loop机制，遇到这种情况，基本是无法优化的。这条语句最终也只能交给应用系统去优化自己的逻辑了。<br/>
<br/>
通过这个例子我们可以看到，并不是所有语句都能优化，而往往我们优化时，由于SQL用例回归时落掉一些极端情况，会造成比原来还严重的后果。<br/>
所以，<br/>
```
第一：不要指望所有语句都能通过SQL优化
第二：不要过于自信，只针对具体case来优化，而忽略了更复杂的情况。
```
<br/><br/>
慢查询的案例就分析到这儿，以上只是一些比较典型的案例。我们在优化过程中遇到过超过1000行，涉及到16个表join的“垃圾SQL”，也遇到过线上线下数据库差异导致应用直接被慢查询拖死，也遇到过varchar等值比较没有写单引号，还遇到过笛卡尔积查询直接把从库搞死。再多的案例启示也只是一些经验的积累，如果我们数据查询优化器、所以的内部原理，那么分析这些案例就变得特别简单了。
<br/><br/>

### 写在后面的话
本文以一个慢查询案例引入了MySQL所以原理、优化慢查询的一些方法论；并针对遇到的典型案例做了详细的分析。其实做了这么长时间的语句优化后发现，任何数据库层面的优化都抵不上应用系统的优化，同样是MySQL，可以用来支撑Google/FaceBook/Taobao应用，但可能连你的个人网站都撑不住。套用最近比较流行的话：“查询容易，优化不易，且写且珍惜！”

#### References

- 1. 《高性能MySQL》

- 2. 《数据结构与算法分析》