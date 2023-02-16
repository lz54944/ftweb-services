#!/bin/bash
nohup java -jar -Dfile.encoding=utf-8 -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5137 /home/workspace/jar/xxx.jar   >/home/workspace/log/xxx.log 2>&1 &
