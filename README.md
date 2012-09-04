Java-Breaking-RSA
=================

A program for factoring very large semiprimes using the quadratic sieve algorithm. For more info about the quadratic sieve algorithm refer to: http://en.wikipedia.org/wiki/Quadratic_sieve

Used to break/crack RSA.

Example output:

FACTORIZING n: 97097221068614748259
FACTORIZER_INPUT_DIR: FACTOR_INPUT
FACTORIZER_INPUT_FILE: input
FACTORIZER_OUTPUT_DIR: FACTOR_OUTPUT

12/09/04 13:55:04 INFO jvm.JvmMetrics: Initializing JVM Metrics with processName=JobTracker, sessionId=
12/09/04 13:55:04 WARN mapred.JobClient: Use GenericOptionsParser for parsing the arguments. Applications should impleme
nt Tool for the same.
12/09/04 13:55:04 INFO input.FileInputFormat: Total input paths to process : 1
12/09/04 13:55:04 INFO mapred.JobClient: Running job: job_local_0001
12/09/04 13:55:04 INFO input.FileInputFormat: Total input paths to process : 1
12/09/04 13:55:04 INFO mapred.MapTask: io.sort.mb = 100
12/09/04 13:55:04 INFO mapred.MapTask: data buffer = 79691776/99614720
12/09/04 13:55:04 INFO mapred.MapTask: record buffer = 262144/327680
12/09/04 13:55:05 INFO mapred.JobClient:  map 0% reduce 0%
12/09/04 13:55:10 INFO mapred.LocalJobRunner:
12/09/04 13:55:10 INFO mapred.JobClient:  map 91% reduce 0%
12/09/04 13:55:11 INFO mapred.MapTask: Starting flush of map output
12/09/04 13:55:11 INFO mapred.MapTask: Finished spill 0
12/09/04 13:55:11 INFO mapred.TaskRunner: Task:attempt_local_0001_m_000000_0 is done. And is in the process of commiting

12/09/04 13:55:11 INFO mapred.LocalJobRunner:
12/09/04 13:55:11 INFO mapred.TaskRunner: Task 'attempt_local_0001_m_000000_0' done.
12/09/04 13:55:11 INFO mapred.MapTask: io.sort.mb = 100
12/09/04 13:55:11 INFO mapred.MapTask: data buffer = 79691776/99614720
12/09/04 13:55:11 INFO mapred.MapTask: record buffer = 262144/327680
12/09/04 13:55:11 INFO mapred.JobClient:  map 100% reduce 0%
12/09/04 13:55:13 INFO mapred.MapTask: Starting flush of map output
12/09/04 13:55:13 INFO mapred.MapTask: Finished spill 0
12/09/04 13:55:13 INFO mapred.TaskRunner: Task:attempt_local_0001_m_000001_0 is done. And is in the process of commiting

12/09/04 13:55:13 INFO mapred.LocalJobRunner:
12/09/04 13:55:13 INFO mapred.TaskRunner: Task 'attempt_local_0001_m_000001_0' done.
12/09/04 13:55:13 INFO mapred.LocalJobRunner:
12/09/04 13:55:13 INFO mapred.Merger: Merging 2 sorted segments
12/09/04 13:55:13 INFO mapred.Merger: Down to the last merge-pass, with 2 segments left of total size: 13962 bytes
12/09/04 13:55:13 INFO mapred.LocalJobRunner:
Successfully factorized n. Results will be written to the output file.
12/09/04 13:55:14 INFO mapred.TaskRunner: Task:attempt_local_0001_r_000000_0 is done. And is in the process of commiting

12/09/04 13:55:14 INFO mapred.LocalJobRunner:
12/09/04 13:55:14 INFO mapred.TaskRunner: Task attempt_local_0001_r_000000_0 is allowed to commit now
12/09/04 13:55:14 INFO output.FileOutputCommitter: Saved output of task 'attempt_local_0001_r_000000_0' to FACTOR_OUTPUT

12/09/04 13:55:14 INFO mapred.LocalJobRunner: reduce > reduce
12/09/04 13:55:14 INFO mapred.TaskRunner: Task 'attempt_local_0001_r_000000_0' done.
12/09/04 13:55:14 INFO mapred.JobClient:  map 100% reduce 100%
12/09/04 13:55:14 INFO mapred.JobClient: Job complete: job_local_0001
12/09/04 13:55:14 INFO mapred.JobClient: Counters: 12
12/09/04 13:55:14 INFO mapred.JobClient:   FileSystemCounters
12/09/04 13:55:14 INFO mapred.JobClient:     FILE_BYTES_READ=133199185
12/09/04 13:55:14 INFO mapred.JobClient:     FILE_BYTES_WRITTEN=134205013
12/09/04 13:55:14 INFO mapred.JobClient:   Map-Reduce Framework
12/09/04 13:55:14 INFO mapred.JobClient:     Reduce input groups=1
12/09/04 13:55:14 INFO mapred.JobClient:     Combine output records=0
12/09/04 13:55:14 INFO mapred.JobClient:     Map input records=15
12/09/04 13:55:14 INFO mapred.JobClient:     Reduce shuffle bytes=0
12/09/04 13:55:14 INFO mapred.JobClient:     Reduce output records=2
12/09/04 13:55:14 INFO mapred.JobClient:     Spilled Records=30
12/09/04 13:55:14 INFO mapred.JobClient:     Map output bytes=13899
12/09/04 13:55:14 INFO mapred.JobClient:     Combine input records=0
12/09/04 13:55:14 INFO mapred.JobClient:     Map output records=15
12/09/04 13:55:14 INFO mapred.JobClient:     Reduce input records=15
FACTOR: 9784331033
FACTOR: 9923746523
Press any key to continue . . .