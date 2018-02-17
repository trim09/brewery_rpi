#!/bin/sh

#PATH=/sbin:/usr/sbin:/bin:/usr/bin

case "$1" in
  start|"")
	sudo kill -9 $(ps -efww | grep "cz.todr.brewery.Main"| grep -v grep | tr -s " "| cut -d" " -f2)
	cd /home/pi/IdeaProjects/brewery-proj
	sudo java -cp classes:lib/'*' cz.todr.brewery.Main &
	;;
  restart|reload|force-reload)
	echo "Error: argument '$1' not supported" >&2
	exit 3
	;;
  stop)
	sudo kill -9 $(ps -efww | grep "cz.todr.brewery.Main"| grep -v grep | tr -s " "| cut -d" " -f2)
	;;
  status)
	ps -efww | grep "cz.todr.brewery.Main"| grep -v grep
	exit $?
	;;
  *)
	echo "Usage: brewery [start|stop|status]" >&2
	exit 3
	;;
esac

:
