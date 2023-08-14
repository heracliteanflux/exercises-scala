python3 fb.py $MR_OPT \
--jobconf mapred.reduce.tasks=4 \
hdfs:///ds410/facebook \
--output-dir lab2 \
--no-output