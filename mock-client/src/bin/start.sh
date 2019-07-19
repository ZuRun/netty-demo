#!/bin/bash

#by sxjia.

cd `dirname $0`
VAR_BIN_DIR=`pwd`
cd ..
VAR_DEPLOY_DIR=`pwd`
VAR_CONF_DIR=$VAR_DEPLOY_DIR/conf
VAR_LOG_DIR=$VAR_DEPLOY_DIR/log
VAR_LIB_DIR=$VAR_DEPLOY_DIR/lib
VAR_LIB_JARS=`ls $VAR_LIB_DIR|grep .jar|awk '{print "'$VAR_LIB_DIR'/"$0}'|tr "\n" ":"`
VAR_STDOUT_FILE=$VAR_LOG_DIR/stdout.log

if [ ! -d $VAR_LOG_DIR ]; then
    mkdir $VAR_LOG_DIR
fi

if [ -n "$JAVA_OPTS" ]; then
    VAR_JAVA_OPTS=$JAVA_OPTS
else
    VAR_JAVA_OPTS="-Xmx4350M -Xms4350M -Xmn4050M -XX:MaxTenuringThreshold=15 -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:log/gc.log -XX:+UseParallelGC -XX:+UseParallelOldGC"
fi

VAR_PRE_JAVA_PROPERTIES=
VAR_JAVA_PROPERTIES="$JAVA_PROPERTIES $VAR_PRE_JAVA_PROPERTIES"

MAIN_CLASS="cn.zull.netty.mock.MockClientApplication"

VAR_JAVA_PATH=""
if [ -n "$JAVA_HOME" ]; then
    VAR_JAVA_PATH="$JAVA_HOME/bin/java"
else
    VAR_JAVA_PATH="java"
fi

nohup $VAR_JAVA_PATH $VAR_JAVA_OPTS -classpath $VAR_CONF_DIR:$VAR_LIB_JARS $VAR_PRE_JAVA_PROPERTIES $MAIN_CLASS > $VAR_STDOUT_FILE 2>&1 &

VAR_PIDS=`ps aux | grep java | grep "$VAR_DEPLOY_DIR" | grep -v grep | grep -v "start.sh" | awk '{print $2}'`
echo "PID: $VAR_PIDS"