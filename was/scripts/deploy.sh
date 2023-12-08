#!/bin/bash

# JAR 파일명에 대소문자 구별이 없도록
shopt -s nocaseglob

sudo userdel -rf ec2-user

sudo dnf install cronie -y >> /home/dodo_admin/backup-deploy.log

sudo systemctl start crond
sudo systemctl enable crond

crontab -r

sudo aws s3 cp s3://dodo-development-bucket/agent.json/was-port-check.sh /usr/bin/

sudo chmod a+rx /usr/bin/was-port-check.sh

sudo sed -i 's/\r$//' /usr/bin/was-port-check.sh

echo "* * * * * /usr/bin/was-port-check.sh" | crontab -

sudo systemctl restart crond

export SSM_DB_ENDPOINT=$(aws ssm get-parameter --name /dodo/db-endpoint --with-decryption --query Parameter.Value --output text)
export SSM_DB_ID=$(aws ssm get-parameter --name /dodo/db-id --with-decryption --query Parameter.Value --output text)
export SSM_DB_PW=$(aws ssm get-parameter --name /dodo/db-pw --with-decryption --query Parameter.Value --output text)
export SSM_REDIS_ENDPOINT=$(aws ssm get-parameter --name /dodo/redis-endpoint --with-decryption --query Parameter.Value --output text)

BUILD_JAR=$(ls /home/dodo_admin/*-SNAPSHOT.jar)
JAR_NAME=$(basename "$BUILD_JAR")
echo "> build 파일명: $JAR_NAME" >> /home/dodo_admin/deploy.log

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/dodo_admin/deploy.log
CURRENT_PID=$(pgrep -f "$JAR_NAME")

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/dodo_admin/deploy.log
else
  echo "> kill -15 $CURRENT_PID" >> /home/dodo_admin/deploy.log
  kill -15 "$CURRENT_PID"
  sleep 10
fi

DEPLOY_JAR="/home/dodo_admin/$JAR_NAME"
echo "> $DEPLOY_JAR 배포"    >> /home/dodo_admin/deploy.log
sudo chmod a+w .
sudo chmod a+w /home/dodo_admin/access.log
sudo chmod a+w /home/dodo_admin/error.log
nohup java -jar "$DEPLOY_JAR" >> /home/dodo_admin/access.log 2> /home/dodo_admin/error.log &

sleep 5

CURRENT_PID=$(pgrep -f "$JAR_NAME")

echo "> 실행된 애플리케이션 PID : $CURRENT_PID"    >> /home/dodo_admin/deploy.log
