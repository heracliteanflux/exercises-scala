Shell

1

```
scala --version
```
```
Scala code runner version 3.3.0 -- Copyright 2002-2023, LAMP/EPFL
```

2

```
sbt --version
```
```
sbt version in this project: 1.9.3
sbt script version: 1.9.3
```

3

```
sbt
```
```sbt
[warn] No sbt.version set in project/build.properties, base directory: /Users/davefriedman/.../L4
[info] welcome to sbt 1.9.3 (Oracle Corporation Java 18.0.2.1)
[info] set current project to l4 (in build file:/Users/davefriedman/.../L4)
[info] sbt server started at local:///Users/davefriedman/.sbt/1.0/server/c4090505782916ce6369/sock
[info] started sbt server
```

4

```sbt
sbt:l4> compile
[info] compiling 2 Scala sources to /Users/davefriedman/.../L4/target/scala-2.12/classes ...
[success] Total time: 2 s, completed Aug 14, 2023, 11:49:28 AM
```

5

```sbt
sbt:l4> run
[info] running Tester1
Starting Tests
Q1
Calling q1_sorted_tuple(4,2,1)...
(1,2,4)
Q2
Calling q2_string_practice(world, 23)...
Hello, world, next year you will be 24.
()
Q3
Calling q3_indexed_sum(List(4,1,2)...
12
Calling q3_indexed_sum(Vector(1,2,3,4)...
30
Q4
Calling q4_indexed_sum(Vector(1,2,3,4))...
30.0
Q5
Calling q5_application with x=1 and f being the function that adds its inputs...
4
Calling q5_application with x=2 and f being the function that subtracts the second from its first input...
-1
Q6
Calling q6_cubed_no2(2,3,4,5,0)...
Answer:
Vector(8, 64, 0)
Result:
Vector(8, 64, 0)
Q7
Calling q7_cubed(5)...
Answer:
Vector(0, 1, 8, 27, 64)
Result:
Vector(0, 1, 8, 27, 64)
Q8
Calling q8_cubed(5)...
Answer:
Vector(0, 1, 8, 27, 64)
Result:
Vector(0, 1, 8, 27, 64)
Q9
Calling q9_find(List(2,4,6,8,10),8)...
Answer:
3
Result:
3
Q10
Calling q10_find(Vector(1,3,4,5,6,7,8),{x=>x%2==0})...
Answer:
2
Result:
2
Q11
Calling q11_tribonacci(0,0,1,11)...
Answer:
81
Result:
81
Q
Calling q12_tribonacci(0,0,1,12)...
Answer:
149
Result:
149
[success] Total time: 0 s, completed Aug 14, 2023, 11:53:25 AM
```