#!/bin/sh

#################################################
#                                               #
#        Configuration for Playce Migrator      #
#                                               #
#################################################

# Set JAVA_HOME when default java version is not JDK 11
# export JAVA_HOME=/usr/lib/jvm/java-11

PORT_OFFSET=3

# Working directory for Migrator
WORKING_DIR=/opt/migrator
ORIGIN_DIR=$WORKING_DIR/origin-dir
WORK_DIR=$WORKING_DIR/work-dir

# DB Connection URL
DB_URL=jdbc:mariadb://localhost:3306/migratordb

# DB Username
DB_USERNAME=playce

# DB Password
DB_PASSWORD=playce

# OAUTH DB Name
OAUTH_DB_NAME=oauthdb

if [ e$WORKING_DIR = "e" ] ; then
    echo "[Error] WORKING_DIR must be set."
    exit;
fi

if [ e$DB_URL = "e" ] ; then
    echo "[Error] DB_URL must be set."
    exit;
fi

if [ e$DB_USERNAME = "e" ] ; then
    echo "[Error] DB_USERNAME must be set."
    exit;
fi

if [ e$DB_PASSWORD = "e" ] ; then
    echo "[Error] DB_PASSWORD must be set."
    exit;
fi

# Log file path
JAVA_OPTS="$JAVA_OPTS -DLOG_PATH=$CATALINA_HOME/logs/"
JAVA_OPTS="$JAVA_OPTS -Dlogging.file.name=$CATALINA_HOME/logs/playce-migrator.log"

# File encoding
JAVA_OPTS="$JAVA_OPTS -Dsun.jnu.encoding=UTF-8 -Dfile.encoding=UTF-8 -Dfile.client.encoding=UTF-8"

# Additional config
JAVA_OPTS="$JAVA_OPTS -Xms2048m -Xmx2048m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseLargePagesInMetaspace"
JAVA_OPTS="$JAVA_OPTS -XX:+ExplicitGCInvokesConcurrent"
JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"
JAVA_OPTS="$JAVA_OPTS -XX:ReservedCodeCacheSize=512m"
JAVA_OPTS="$JAVA_OPTS -XX:-UseCodeCacheFlushing"
JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/urandom"
JAVA_OPTS="$JAVA_OPTS -Djava.net.preferIPv4Stack=true"
JAVA_OPTS="$JAVA_OPTS -Djava.security.properties=$CATALINA_HOME/conf/security/java.security"

JAVA_OPTS="$JAVA_OPTS -Dspring.datasource.url='$DB_URL'"
JAVA_OPTS="$JAVA_OPTS -Dspring.datasource.username='$DB_USERNAME'"
JAVA_OPTS="$JAVA_OPTS -Dspring.datasource.password='$DB_PASSWORD'"
JAVA_OPTS="$JAVA_OPTS -D=playce-migrator.oauth-db-name='$OAUTH_DB_NAME'"


JAVA_OPTS="$JAVA_OPTS -Dhttp.port=$(expr 8080 + $PORT_OFFSET)"
JAVA_OPTS="$JAVA_OPTS -Dajp.port=$(expr 8009 + $PORT_OFFSET)"
JAVA_OPTS="$JAVA_OPTS -Dssl.port=$(expr 8443 + $PORT_OFFSET)"
JAVA_OPTS="$JAVA_OPTS -Dshutdown.port=$(expr 8005 + $PORT_OFFSET)"

# application config
JAVA_OPTS="$JAVA_OPTS -Dscheduler-config.toggle.enable-analysis=true"
JAVA_OPTS="$JAVA_OPTS -Dscheduler-config.toggle.enable-migration=true"

# Migrator working directory
JAVA_OPTS="$JAVA_OPTS -Dplayce-migrator.origin-dir=$ORIGIN_DIR"
JAVA_OPTS="$JAVA_OPTS -Dplayce-migrator.work-dir=$WORK_DIR"
