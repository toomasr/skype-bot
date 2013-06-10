#!/bin/sh

set -e
cd $appHome
cp cert.pem expanded.pem
base64 -d < expanded.pem > cert.pem
openssl pkcs8 -topk8 -nocrypt -inform PEM -outform DER -in cert.pem -out cert.der

