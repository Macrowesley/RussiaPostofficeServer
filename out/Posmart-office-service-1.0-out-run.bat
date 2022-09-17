
java  -Xms1G -Xmx1G -Xss256k -XX:MetaspaceSize=100M -XX:+UseParallelOldGC -XX:NewSize=1G -XX:MaxNewSize=1G -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -jar Posmart-office-service-1.0.jar --spring.config.location=./application-out.yml
