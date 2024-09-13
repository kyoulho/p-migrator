#!/bin/bash

## Make sure prerequisite environment variables are set
#if [ -z "$JAVA_HOME" ]; then
#    if $darwin; then
#        if [ -x '/usr/libexec/java_home' ] ; then
#            export JAVA_HOME=`/usr/libexec/java_home`
#        elif [ -d "/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home" ]; then
#            export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home"
#        fi
#    else
#        JAVA_PATH=`which java 2> /dev/null`
#        if [ "x$JAVA_PATH" != "x" ]; then
#            JAVA_PATH=`dirname "$JAVA_PATH" 2> /dev/null`
#            JAVA_HOME=`dirname "$JAVA_PATH" 2> /dev/null`
#        fi
#    fi
#
#    if [ "x$JAVA_HOME" = "x" ]; then
#        if [ -x /usr/bin/java ]; then
#            JAVA_HOME=/usr
#        elif [ -x /bin/java ]; then
#            JAVA_HOME=/
#        fi
#    fi
#fi

if [ ! e$1 = "e" ]; then
  JDEPRSCAN_PATH=$1
fi

if [ -z "$JDEPRSCAN_PATH" ] || [ ! -x "$JDEPRSCAN_PATH"/bin/jdeprscan ]; then
    echo "[ERROR] jdeprscan does not exist."
    exit
fi

if [ e$2 = "e--version" ]; then
    $JDEPRSCAN_PATH/bin/jdeprscan --version
else
    $JDEPRSCAN_PATH/bin/jdeprscan --release $2 $3
fi