# .bashrc

# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

# Uncomment the following line if you don't like systemctl's auto-paging feature:
# export SYSTEMD_PAGER=

# User specific aliases and functions
JAVA_HOME=/usr/jdk64/jdk1.8.0_112/
HS_JAR=/usr/hdp/3.1.0.0-78/hadoop-mapreduce/hadoop-streaming.jar
MR_OPT="-r hadoop --hadoop-streaming-jar $HS_JAR"

export HS_JAR
export MR_OPT
export PATH=$JAVA_HOME/bin:$PATH