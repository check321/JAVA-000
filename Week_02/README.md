# Week_02_GC_Analysis

[TOC]

## SerialGC
` java -Xms512m -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+UseSerialGC GCLogAnalysis`

### MinorGC
> 0.144: [GC (Allocation Failure) 0.144: [DefNew: 139601K->17472K(157248K), 0.0285125 secs] 139601K->46640K(506816K), 0.0285459 secs] [Times: user=0.02 sys=0.01, real=0.03 secs]

| 分代   |    分代使用率  |  堆使用率   | GC耗时|
| :-------- | --------:| :------: |:------: |
| Young    |   11.11% | 9.2% | 0.03s|

### MajorGC
> 3.038: [Full GC (Allocation Failure) 3.038: [Tenured: 349545K->349326K(349568K), 0.0556109 secs] 506309K->388848K(506816K), [Metaspace: 2725K->2725K(1056768K)], 0.0556544 secs] [Times: user=0.05 sys=0.00, real=0.06 secs]
3.093: Total time for which application threads were stopped: 0.0557547 seconds, Stopping threads took: 0.0000162 seconds

| 分代   |    分代使用率  |  堆使用率   | GC耗时|
| :-------- | --------:| :------: |:------: |
|Old    |   99.99% | 76.72% | 0.05s|

### Heap

`执行结束!共生成对象次数:22140`
> Heap
 def new generation   total 157248K, used 45292K [0x00000007a0000000, 0x00000007aaaa0000, 0x00000007aaaa0000)
  eden space 139776K,  32% used [0x00000007a0000000, 0x00000007a2c3b2d8, 0x00000007a8880000)
  from space 17472K,   0% used [0x00000007a9990000, 0x00000007a9990000, 0x00000007aaaa0000)
  to   space 17472K,   0% used [0x00000007a8880000, 0x00000007a8880000, 0x00000007a9990000)
 tenured generation   total 349568K, used 349326K [0x00000007aaaa0000, 0x00000007c0000000, 0x00000007c0000000)
   the space 349568K,  99% used [0x00000007aaaa0000, 0x00000007bffc3900, 0x00000007bffc3a00, 0x00000007c0000000)
 Metaspace       used 2732K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 296K, capacity 386K, committed 512K, reserved 1048576K

| 分代   |   使用空间  |  总空间   | 使用率|
| :-------- | --------:| :------: |:------: |
|New    |   45292K | 157248K | 28.8%|
|New(Eden)    |   139776K | 139776K | 28.8%|

## ParallelGC

`java -Xms512m -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+UseParallelGC GCLogAnalysis`

### MinorGC

> 0.144: [GC (Allocation Failure) [PSYoungGen: 131584K->21499K(153088K)] 131584K->47842K(502784K), 0.0191793 secs] [Times: user=0.03 sys=0.09, real=0.02 secs]
| 分代   |    分代使用率  |  堆使用率   | GC耗时|
| :-------- | --------:| :------: |:------: |
|Young    |   14.04% | 9.51% | 0.019s|

### MajorGC(Ergonomics) 

>3.045: [Full GC (Ergonomics) [PSYoungGen: 58878K->17650K(116736K)] [ParOldGen: 349343K->349334K(349696K)] 408222K->366984K(466432K), [Metaspace: 2725K->2725K(1056768K)], 0.0615817 secs] [Times: user=0.40 sys=0.01, real=0.07 secs]
3.107: Total time for which application threads were stopped: 0.0617411 seconds, Stopping threads took: 0.0000150 seconds

| 分代   |    分代使用率  |  堆使用率   | GC耗时|
| :-------- | --------:| :------: |:------: |
|Young   |   15.11% | - | 0.05s|
|Old   |   99.89% | 78.67%| 0.07s|

*[Times: user=0.40 sys=0.01, real=0.07 secs] user+sys=0.41s 但实际GC时间只有0.41 / 0.07 ≈ 6 ，说明ParallelGC使用6个线程并行GC，大幅缩短GC时间*


### Heap
> 执行结束!共生成对象次数:13670
Heap
 PSYoungGen      total 116736K, used 19737K [0x00000007b5580000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 58880K, 33% used [0x00000007b5580000,0x00000007b68c6730,0x00000007b8f00000)
  from space 57856K, 0% used [0x00000007b8f00000,0x00000007b8f00000,0x00000007bc780000)
  to   space 57856K, 0% used [0x00000007bc780000,0x00000007bc780000,0x00000007c0000000)
 ParOldGen       total 349696K, used 349334K [0x00000007a0000000, 0x00000007b5580000, 0x00000007b5580000)
  object space 349696K, 99% used [0x00000007a0000000,0x00000007b5525bb0,0x00000007b5580000)
 Metaspace       used 2732K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 296K, capacity 386K, committed 512K, reserved 1048576K

## CMS_GC

`java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xms512m -Xmx512m -XX:+PrintGCApplicationStoppedTime -XX:+UseConcMarkSweepGC GCLogAnalysis`

 > 3.013: [GC (CMS Initial Mark) [1 CMS-initial-mark: 349415K(349568K)] 386958K(506816K), 0.0002173 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
3.014: Total time for which application threads were stopped: 0.0002990 seconds, Stopping threads took: 0.0000349 seconds
3.014: [CMS-concurrent-mark-start]
3.015: [CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
3.015: [CMS-concurrent-preclean-start]
3.016: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
3.016: [CMS-concurrent-abortable-preclean-start]
3.016: [CMS-concurrent-abortable-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
3.016: [GC (CMS Final Remark) [YG occupancy: 58639 K (157248 K)]3.016: [Rescan (parallel) , 0.0010363 secs]3.017: [weak refs processing, 0.0000211 secs]3.017: [class unloading, 0.0002425 secs]3.017: [scrub symbol table, 0.0003437 secs]3.018: [scrub string table, 0.0001668 secs][1 CMS-remark: 349415K(349568K)] 408054K(506816K), 0.0018817 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
3.018: Total time for which application threads were stopped: 0.0019776 seconds, Stopping threads took: 0.0000530 seconds
3.018: [CMS-concurrent-sweep-start]
3.018: [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
3.018: [CMS-concurrent-reset-start]
3.019: [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
3.032: [GC (Allocation Failure) 3.032: [ParNew: 157143K->157143K(157248K), 0.0000190 secs]3.032: [CMS: 349109K->349455K(349568K), 0.0549820 secs] 506253K->386965K(506816K), [Metaspace: 2725K->2725K(1056768K)], 0.0550526 secs] [Times: user=0.06 sys=0.00, real=0.06 secs]
3.087: Total time for which application threads were stopped: 0.0551532 seconds, Stopping threads took: 0.0000140 seconds

### 分段GC

- CMS Initial Mark: 初始标记GC Roots引用，需要STW,耗时 0.0002173 s
- CMS-concurrent-mark：并发标记，由InitialMark标记的对象出发做可达性标记，并发执行
-  CMS-concurrent-preclean / CMS-concurrent-abortable-preclean： 可中断预清理，因为CMS-Remark阶段需要扫描整堆去准确的做出可达性分析（跨代引用问题），所以Preclean阶段是为了在Remark前执行一次MinorGC以减少扫描整堆的耗时，同时CMS为了避免Remark过久的等待这次MinorGC导致最终GC时间反而过长，所以将preclean设置成可中断，当等待时间超过`CMSMaxAbortablePrecleanTime`时，CMS会主动中断preclean直接进入Remark阶段
- CMS Final Remark：最终标记阶段，需要STW，以Young区做可达性分析，标记最终存活的对象：`[YG occupancy: 58639 K (157248 K)]3.016: [Rescan (parallel) , 0.0010363 secs] `并行扫描Young区，耗时0.0010363
- CMS-concurrent-sweep-start：并发清理阶段
- CMS-concurrent-reset：重置并发标记，准备下次CMS-GC

### Heap

`[ParNew: 157143K->157143K(157248K), 0.0000190 secs]3.032: [CMS: 349109K->349455K(349568K), 0.0549820 secs] 506253K->386965K(506816K), [Metaspace: 2725K->2725K(1056768K)], 0.0550526 secs] [Times: user=0.06 sys=0.00, real=0.06 secs]`

ParNew 157143K->157143K(157248K) /  [CMS: 349109K->349455K(349568K)：  JVM显示bug，无法看到真实容量

506253K->386965K(506816K):  堆区利用率76.35%

`[Times: user=0.06 sys=0.00, real=0.06 secs]` GC耗时0.06s，因为多阶段分次STW的缘故，GC时长并不出众

` 执行结束!共生成对象次数:25053`
但是CMS有并发GC的能力，多数阶段可与业务线程并发执行，从而大大提高了对象生成次数。


## G1GC

### G1 4g内存
>   2020-10-23 java -XX:+PrintGC -XX:+PrintGCTimeStamps -Xms4g -Xmx4g -XX:+UseG1GC GCLogAnalysis
正在执行...
0.159: [GC pause (G1 Evacuation Pause) (young) 204M->72M(4096M), 0.0200247 secs]
0.200: [GC pause (G1 Evacuation Pause) (young) 250M->122M(4096M), 0.0157952 secs]
0.234: [GC pause (G1 Evacuation Pause) (young) 300M->180M(4096M), 0.0170604 secs]
0.270: [GC pause (G1 Evacuation Pause) (young) 358M->234M(4096M), 0.0164907 secs]
0.304: [GC pause (G1 Evacuation Pause) (young) 412M->296M(4096M), 0.0188828 secs]
0.341: [GC pause (G1 Evacuation Pause) (young) 474M->358M(4096M), 0.0212624 secs]
0.379: [GC pause (G1 Evacuation Pause) (young) 536M->413M(4096M), 0.0166010 secs]
0.415: [GC pause (G1 Evacuation Pause) (young) 611M->481M(4096M), 0.0241133 secs]
0.472: [GC pause (G1 Evacuation Pause) (young) 723M->552M(4096M), 0.0236097 secs]
0.521: [GC pause (G1 Evacuation Pause) (young) 814M->634M(4096M), 0.0258902 secs]
1.459: [GC pause (G1 Evacuation Pause) (young) 3052M->975M(4096M), 0.1123164 secs]
1.620: [GC pause (G1 Evacuation Pause) (young) 1409M->989M(4096M), 0.0707927 secs]
1.730: [GC pause (G1 Evacuation Pause) (young) 1411M->1092M(4096M), 0.0451256 secs]
1.839: [GC pause (G1 Evacuation Pause) (young) 1726M->1242M(4096M), 0.0436506 secs]
1.958: [GC pause (G1 Evacuation Pause) (young) 1998M->1391M(4096M), 0.0465551 secs]
2.110: [GC pause (G1 Evacuation Pause) (young) 2347M->1561M(4096M), 0.0359268 secs]
2.275: [GC pause (G1 Evacuation Pause) (young) 2725M->1740M(4096M), 0.0342717 secs]
2.479: [GC pause (G1 Evacuation Pause) (young) 3244M->1908M(4096M), 0.0361540 secs]
2.662: [GC pause (G1 Evacuation Pause) (young) 3294M->2038M(4096M), 0.0359183 secs]
2.831: [GC pause (G1 Evacuation Pause) (young) 3330M->2181M(4096M), 0.0373775 secs]
2.992: [GC pause (G1 Evacuation Pause) (young) (initial-mark) 3359M->2338M(4096M), 0.0381728 secs]
3.030: [GC concurrent-root-region-scan-start]
3.030: [GC concurrent-root-region-scan-end, 0.0002318 secs]
3.030: [GC concurrent-mark-start]
3.049: [GC concurrent-mark-end, 0.0184493 secs]
3.049: [GC remark, 0.0015383 secs]
3.051: [GC cleanup 2462M->786M(4096M), 0.0028427 secs]
3.054: [GC concurrent-cleanup-start]
3.055: [GC concurrent-cleanup-end, 0.0012950 secs]
执行结束!共生成对象次数:52331

### 对照组 ParallelGC 4g内存

>  2020-10-23 java -XX:+PrintGC -XX:+PrintGCTimeStamps -Xms4g -Xmx4g -XX:+UseParallelGC GCLogAnalysis
正在执行...
0.488: [GC (Allocation Failure)  1048576K->235283K(4019712K), 0.0724047 secs]
0.687: [GC (Allocation Failure)  1283859K->350452K(4019712K), 0.0927017 secs]
0.888: [GC (Allocation Failure)  1399028K->472752K(4019712K), 0.0699056 secs]
1.063: [GC (Allocation Failure)  1521328K->597432K(4019712K), 0.0693821 secs]
1.239: [GC (Allocation Failure)  1646008K->719903K(4019712K), 0.0705488 secs]
1.419: [GC (Allocation Failure)  1768479K->842627K(3702272K), 0.0691507 secs]
1.560: [GC (Allocation Failure)  1573763K->945541K(3860992K), 0.0300010 secs]
1.680: [GC (Allocation Failure)  1676677K->981253K(3855360K), 0.0356206 secs]
1.804: [GC (Allocation Failure)  1711365K->1005440K(3838976K), 0.0384672 secs]
1.928: [GC (Allocation Failure)  1735552K->1030434K(3857408K), 0.0409281 secs]
2.048: [GC (Allocation Failure)  1752866K->1088366K(3709952K), 0.0517356 secs]
2.172: [GC (Allocation Failure)  1810798K->1189328K(3830272K), 0.0424962 secs]
2.285: [GC (Allocation Failure)  1874384K->1268816K(3653632K), 0.0416354 secs]
2.398: [GC (Allocation Failure)  1953872K->1356754K(3822592K), 0.0375163 secs]
2.503: [GC (Allocation Failure)  2018258K->1454337K(3635712K), 0.0419256 secs]
2.615: [GC (Allocation Failure)  2115841K->1541148K(3825152K), 0.0399510 secs]
2.722: [GC (Allocation Failure)  2200604K->1636086K(3825152K), 0.0389937 secs]
2.827: [GC (Allocation Failure)  2295542K->1735239K(3834368K), 0.0400142 secs]
2.936: [GC (Allocation Failure)  2406983K->1822782K(3828224K), 0.0417255 secs]
3.046: [GC (Allocation Failure)  2494526K->1923594K(3846656K), 0.0409170 secs]
执行结束!共生成对象次数:60653



# wrk - benchmark

## SerialGC

`java -jar notifier-demo-0.0.1-SNAPSHOT.jar -XX:UseSerialGC -Xms4g -Xmx4g`

> wrk -t8 -c400 -d30s --latency http://127.0.0.1:25030/benchmark/foo/fyang
Running 30s test @ http://127.0.0.1:25030/benchmark/foo/fyang
  8 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    13.96ms   51.72ms 725.05ms   96.51%
    Req/Sec     4.77k     2.48k   11.34k    58.66%
  Latency Distribution
     50%    5.15ms
     75%    6.54ms
     90%   10.70ms
     99%  253.22ms
  1115995 requests in 30.09s, 130.04MB read
  Socket errors: connect 155, read 244, write 0, timeout 0
Requests/sec:  37090.91
Transfer/sec:      4.32MB


## ParallelGC
`java -jar -XX:+UseParallelGC -Xms4g -Xmx4g notifier-demo-0.0.1-SNAPSHOT.jar`
>wrk -t8 -c400 -d30s --latency http://127.0.0.1:25030/benchmark/foo/fyang
Running 30s test @ http://127.0.0.1:25030/benchmark/foo/fyang
  8 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     8.92ms   22.67ms 569.52ms   96.09%
    Req/Sec     5.17k     2.71k   15.83k    64.53%
  Latency Distribution
     50%    4.86ms
     75%    6.02ms
     90%    8.88ms
     99%  117.44ms
  1232163 requests in 30.10s, 143.58MB read
  Socket errors: connect 155, read 211, write 0, timeout 0
Requests/sec:  40930.59
Transfer/sec:      4.77MB

## CMS_GC
`wrk -t8 -c400 -d30s --latency http://127.0.0.1:25030/benchmark/foo/fyang`
>wrk -t4 -c400 -d30s --latency http://127.0.0.1:25030/benchmark/foo/fyang
Running 30s test @ http://127.0.0.1:25030/benchmark/foo/fyang
  4 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    10.75ms   52.09ms 723.15ms   97.44%
    Req/Sec    14.59k     3.04k   21.49k    85.78%
  Latency Distribution
     50%    3.62ms
     75%    4.16ms
     90%    5.14ms
     99%  265.72ms
  1658500 requests in 30.10s, 193.26MB read
  Socket errors: connect 151, read 152, write 0, timeout 0
Requests/sec:  55095.38
Transfer/sec:      6.42MB


## G1GC

`java -jar -XX:+UseG1GC -Xms4g -Xmx4g notifier-demo-0.0.1-SNAPSHOT.jar`
> wrk -t4 -c400 -d30s --latency http://127.0.0.1:25030/benchmark/foo/fyang
Running 30s test @ http://127.0.0.1:25030/benchmark/foo/fyang
  4 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.13ms   14.83ms 197.34ms   97.42%
    Req/Sec    13.73k     2.88k   23.64k    82.85%
  Latency Distribution
     50%    3.98ms
     75%    4.58ms
     90%    5.53ms
     99%   94.73ms
  1626321 requests in 30.06s, 189.51MB read
  Socket errors: connect 151, read 150, write 0, timeout 0
Requests/sec:  54103.57
Transfer/sec:      6.30MB


### 总结

综上测试结果，在多核cpu4g内存条件下：

- SerialGC无论是成功并发数还是qps都趋于明显劣势，一般生产环境不推荐使用
- ParalleGC得益于并行GC能力，可以很好的控制GC停顿时间，并发请求下qps上强于SerialGC
- CMSGC的分阶段GC将传统GC的STW粒度更加细化，极大的提高了系统的并发能力，在同等单位时间的吞吐能力（1658500）大大好于前两种GC
- G1GC革命性的改变了GC分代的思想，作为CMS并发GC的一种很遗憾在测试数据中没有提现出在大内存heap下的优势。