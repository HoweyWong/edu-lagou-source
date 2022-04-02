source /etc/profile > /dev/null
jarPackage="$1"
 
ps aux|grep -w ${jarPackage} |grep -v grep >/dev/null && ps aux|grep -w ${jarPackage} |egrep -v "grep|stop.sh"|awk '{print $2}'|xargs kill

sleep 3

ps aux|grep -w ${jarPackage} |grep -v grep >/dev/null && ps aux|grep -w ${jarPackage} |egrep -v "grep|stop.sh"|awk '{print $2}'|xargs kill -9

echo $?
