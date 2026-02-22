#!/bin/sh
set -e
PORT=${PORT:-8080}
# Use PORT from Render; limit heap for free tier (512MB)
exec java -Xmx256m -Dserver.address=0.0.0.0 -Dserver.port="$PORT" -jar /app/app.jar
