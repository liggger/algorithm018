# 学习笔记

## 分治代码模板

```python
def divide_conquer(problem, param1, param2, ...):
	if problem is None:
		print_result
		return
	# prepare data
	data = prepare_data(problem)
	subproblems = split_problem(problem, data)
	# conquer subproblems
	subresult1 = self.divide_conquer(subproblems[0], p1, ...)
	subresult2 = self.divide_conquer(subproblems[1], p1, ...)
	subresult3 = self.divide_conquer(subproblems[2], p1, ...)
	...
	# process and generate the final result
	result = process_result(subresult1, subresult2, subresult3, ...)
```

```python
def recursion(level, param1, param2, ...):
	if level > MAX_LEVEL:
		process_result
		return
	process(level, data...)
	self.recursion(level + 1, p1, ...)
```