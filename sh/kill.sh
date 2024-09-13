#!/bin/sh

PID=`ps -ef | grep java | grep tomcat | grep -E "playce-migrator($|\s)" | awk '{print $2}'`

if [ e$PID == "e" ] ; then
    logger -s "playce-migrator is not running."
    exit;
fi

ps -ef | grep java | grep tomcat | grep -E "playce-migrator($|\s)" | awk {'print "kill -9 " $2'} | sh -x