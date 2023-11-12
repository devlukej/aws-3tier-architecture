#!/bin/bash
BUILD_JAR=$(ls /home/ec2-user/*-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/deploy.log

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/deploy.log
CURRENT_PID=$(ps -ef | grep java | grep *-SNAPSHOT.jar | awk '{print $2}')

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_JAR=/home/ec2-user/$JAR_NAME
echo "> $DEPLOY_JAR 배포"    >> /home/ec2-user/deploy.log

nohup java -jar $DEPLOY_JAR >> /home/ec2-user/access.log 2> /home/ec2-user/error.log &
