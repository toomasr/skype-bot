#!/bin/sh

set -e

# find pid file
cd $appHome
[ -f skype-bot.pid ] &&kill `cat skype-bot.pid`
echo "Will sleep 5 to let everything die"
sleep 5
#don't forget to exit, otherwise deploy can't finish
java -jar lib/skype-bot-0.0.1-SNAPSHOT.jar &