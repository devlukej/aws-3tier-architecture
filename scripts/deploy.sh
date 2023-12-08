#!/bin/bash

sudo userdel -rf ec2-user

DEPLOY_SOURCE='/home/dodo_admin/dodo/src/main/resources/templates/*.html'
NGINX_SOURCE='/usr/share/nginx/html/'

echo "--------Nginx Setting--------" >> /home/dodo_admin/deploy.log
echo ">Copy Templates LIST : $(ls -la $DEPLOY_SOURCE)" >> /home/dodo_admin/deploy.log

echo "> Copying to $NGINX_SOURCE" >> /home/dodo_admin/deploy.log
sudo cp $DEPLOY_SOURCE $NGINX_SOURCE

echo "> Copied Templates LIST : $(ls -la $NGINX_SOURCE)" >> /home/dodo_admin/deploy.log

echo "--------Restart Nginx--------" >> /home/dodo_admin/deploy.log
sudo systemctl restart nginx
echo "--------Setting Done--------" >> /home/dodo_admin/deploy.log


echo "--------Backup Setting--------" >> /home/dodo_admin/backup-deploy.log
sudo chmod a+w .
sudo chmod +x /home/dodo_admin/dodo/scripts/backup-cron.sh

echo "--------Install Cron--------" >> /home/dodo_admin/backup-deploy.log
sudo dnf install cronie -y >> /home/dodo_admin/backup-deploy.log
echo "----------------------------" >> /home/dodo_admin/backup-deploy.log

sudo systemctl start crond
sudo systemctl enable crond

crontab -r

sudo aws s3 cp s3://dodo-development-bucket/agent.json/web-port-check.sh /usr/bin/

sudo chmod a+rx /usr/bin/web-port-check.sh

sudo sed -i 's/\r$//' /usr/bin/web-port-check.sh

echo "--------Cron Setting--------" >> /home/dodo_admin/backup-deploy.log
(echo "0 3 * * * /home/dodo_admin/dodo/scripts/backup-cron.sh" && echo "* * * * * /usr/bin/web-port-check.sh") | crontab -
echo | crontab -l >> /home/dodo_admin/backup-deploy.log 2>&1
echo "----------------------------" >> /home/dodo_admin/backup-deploy.log

echo "--------Start Cron--------" >> /home/dodo_admin/backup-deploy.log
sudo systemctl restart crond
sudo service crond status >> /home/dodo_admin/backup-deploy.log
echo "--------------------------" >> /home/dodo_admin/backup-deploy.log

echo "--------Setting Done--------" >> /home/dodo_admin/backup-deploy.log

