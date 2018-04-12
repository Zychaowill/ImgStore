Please remember these shortcut command:
```bash
x -> .
dw -> .
dd -> .
cw -> .

A; -> j. -> j.  etc.
```

#### 一箭双雕

- C => c$

- s => cl

- S => ^C

- I => ^i

- A => $a

- o => A<CR>

- O => ko => kA<CR> => k$a<CR>

#### 可重复的操作及如何回退:

- 做出一个修改 
	- {edit}  
	- .   
	- u

- 在行内查找下一指定字符 
	- f{char}/t{char} 
	- ; 
	- ,
	
- 在行内查找上一指定字符
	- F{char}/T{char}
	- ;
	- ,

- 在文档中查找下一处匹配项
	- /pattern/<CR>
	- n
	- N
	
- 在文档中查找上一处匹配项
	- ?pattern/<CR>
	- n
	- N
	
- 执行替换
	- :s/target/replacement
	- &
	- u
	
- 执行一系列修改
	- qx{changes}q
	- @x
	- u