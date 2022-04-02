source /etc/profile > /dev/null
jarPackage="$1"

nohup java -Dcsp.sentinel.dashboard.server=172.16.179.134:8080 -Dproject.name=edu-front-boot -server -Xmx1344M -Xms1344M -Xmn448M -XX:MaxMetaspaceSize=256M -XX:MetaspaceSize=256M -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -jar ${jarPackage} > /dev/null &

echo $?