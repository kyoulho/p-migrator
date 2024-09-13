#!/bin/bash

PRIV=/var/jenkins_home/.ssh/id_rsa
APP_NAME=migrator
SOURCE='playce-migrator-api/target/playce-'$APP_NAME'.war'
TARGET_DIR=/opt/playce/playce-$APP_NAME

echo 'copy config'
sed 's/OAUTH_DB_NAME=oauthdb/OAUTH_DB_NAME='$OAUTH_DB_NAME'/' sh/setenv.sh > /tmp/setenv.sh
scp -i $PRIV /tmp/setenv.sh sh/kill.sh osc@$SERVER:$TARGET_DIR/bin/
rm -f /tmp/setenv.sh
scp -i $PRIV sh/server.xml osc@$SERVER:$TARGET_DIR/conf/
echo 'copy app'
scp -i $PRIV $SOURCE 'osc@'$SERVER:$TARGET_DIR'/webapps/'$APP_NAME'.war'
echo 'unzip app'
ssh -i $PRIV osc@$SERVER 'cd '$TARGET_DIR'/webapps && rm -rf '$APP_NAME' && unzip -d '$APP_NAME' '$APP_NAME'.war && rm -f '$APP_NAME'.war'
