#!/bin/bash

# JAR 파일명에 대소문자 구별이 없도록
shopt -s nocaseglob

BUILD_JAR=$(ls /home/ec2-user/*-SNAPSHOT.jar)
JAR_NAME=$(basename "$BUILD_JAR")
echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/deploy.log

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ec2-user/deploy.log
CURRENT_PID=$(pgrep -f "$JAR_NAME")

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/deploy.log
else
  echo "> kill -15 $CURRENT_PID" >> /home/ec2-user/deploy.log
  kill -15 "$CURRENT_PID"
  sleep 10
fi

DEPLOY_JAR="/home/ec2-user/$JAR_NAME"
echo "> $DEPLOY_JAR 배포"    >> /home/ec2-user/deploy.log
sudo chmod a+w .
sudo chmod a+w /home/ec2-user/access.log
sudo chmod a+w /home/ec2-user/error.log
nohup java -jar "$DEPLOY_JAR" >> /home/ec2-user/access.log 2> /home/ec2-user/error.log &

sleep 5

CURRENT_PID=$(pgrep -f "$JAR_NAME")

echo "> 실행된 애플리케이션 PID : $CURRENT_PID"    >> /home/ec2-user/deploy.log
