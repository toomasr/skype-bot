#!/bin/sh

set -e

# find pid file
cd $appHome
[ -f skype-bot.pid ] && kill `cat skype-bot.pid`
rm skype-bot.pid
rm expanded.pem
rm cert.pem
rm cert.der