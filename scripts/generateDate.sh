#!/bin/bash
a="insert into TemperatureHistoryRecord (temp, time) values ('"
b="', {ts '"
c="'});"

#2012-09-17 19:12:52.69'});

for i in `seq 1 9`;
do
    for j in `seq 10 59 `;
    do
        for k in `seq 10 59`;
	    do
		echo "$a$i$j$k${b}2012-09-17 0$i:$j:$k$c"
    	    done 
	    
    done
done
