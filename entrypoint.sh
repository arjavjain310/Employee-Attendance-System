#!/bin/sh
set -e
PORT=${PORT:-8080}
exec java -Dserver.address=0.0.0.0 -Dserver.port=$PORT -jar app.jar
