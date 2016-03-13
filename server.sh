#!/bin/bash

name=webnoir
pidfile=/var/run/$name.pid

if [ -f "$pidfile" ]; then
    pid=`cat $pidfile`
    running=`ps p $pid |wc -l`
    if [ $running -eq 1 ]; then
        pid=
    fi
else
    pid=
fi

case $1 in
    start)
        if [ "$pid" = "" ]; then
            nohup lein run prod 2>&1 1>>/var/log/$name.log &
            echo $! > $pidfile
        fi
        $0 status
        ;;
    stop)
        if [ "$pid" = "" ]; then
            echo $name - Not running
        else
            echo Stopping $name
            kill $pid
        fi
		if [ -f $pidfile ]; then
			rm $pidfile
		fi
        ;;
    restart)
        $0 stop
        $0 start
        ;;
    status)
        if [ "$pid" = "" ]; then
            echo $name stopped
        else
            echo $name running with PID $pid
        fi
        ;;
    *)
        echo $0 "(stop | start | restart | status)"
        ;;
esac
