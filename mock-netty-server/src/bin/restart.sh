#!/bin/bash
echo "结束已有进程..."
sh ./kill.sh
sleep 3
echo "重新启动应用..."
sh ./start.sh
tail -f ../log/stdout.log
