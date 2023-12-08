#!/bin/bash

# Set your S3 bucket and folder
S3_BUCKET="dodo-development-bucket"
BACKUP_FOLDER="backup"

# Create a timestamp for the backup file
TIMESTAMP=$(date "+%Y%m%d")
BACKUP_FILE="$TIMESTAMP-dodo-source.zip"

echo "----- 압축 시작 -----" >> /home/dodo_admin/backup.log
zip -r $BACKUP_FILE /home/dodo_admin/dodo/* >> /home/dodo_admin/backup.log 2>&1
echo "----- 압축 완료 -----" >> /home/dodo_admin/backup.log

echo "----- S3 업로드 시작 -----" >> /home/dodo_admin/backup.log
aws s3 cp $BACKUP_FILE s3://$S3_BUCKET/$BACKUP_FOLDER/ >> /home/dodo_admin/backup.log 2>&1
echo "----- S3 업로드 완료 -----" >> /home/dodo_admin/backup.log

echo "----- 알집 삭제 시작 -----" >> /home/dodo_admin/backup.log
rm $BACKUP_FILE
echo "$BACKUP_FILE 삭제 중" >> /home/dodo_admin/backup.log 2>&1
echo "----- 알집 삭제 완료 -----" >> /home/dodo_admin/backup.log