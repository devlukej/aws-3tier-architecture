#!/bin/bash

DEPLOY_SOURCE='/home/ec2-user/*.html'
NGINX_SOURCE='/usr/share/nginx/html/'

echo ">Copy Templates LIST : $(ls -la $DEPLOY_SOURCE)" >> /home/ec2-user/deploy.log

echo "> Copying to $NGINX_SOURCE" >> /home/ec2-user/deploy.log
sudo cp $DEPLOY_SOURCE $NGINX_SOURCE

echo "> Copied Templates LIST : $(ls -la $NGINX_SOURCE)" >> /home/ec2-user/deploy.log

echo "--------Restart Nginx--------" >> /home/ec2-user/deploy.log
sudo systemctl restart nginx