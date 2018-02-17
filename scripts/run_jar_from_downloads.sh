#!/bin/bash
src="/home/pi/downloads"
dest="/home/pi/sandbox/shm"

found=`ls -ct $src/*.tar.gz | head -n1`

if [ -f "$found" ]; then
    echo "Found $found"
else 
    echo "no archive"
    exit 1
fi

dirName=`tar -tf $found | grep -o '^[^/]\+' | sort -u`
appRootDir="$dest/$dirName"
if [ -d  "$appRootDir" ]; then
    echo "Removing old directory: $appRootDir" 
    rm -r "$appRootDir"
fi

echo "Deompressing $found"
tar -xzf "$found" -C "$dest"

appJar=`ls $appRootDir/*.jar`
echo "Found jar $appJar"

remote_debug="-Xdebug -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n"
jmx="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8999 -Dcom.sun.management.jmxremote.ssl=fals -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=raspberrypi -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints "
#run_app="java $remote_debug $jmx -jar $appJar -t 123.456"
run_app="java -jar $appJar -t 123.456"
echo "$run_app"
echo ""
$run_app
